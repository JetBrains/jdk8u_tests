/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

/** 
 * @author Michal Cierniak, James Stichnoth, Salikh Zakirov
 * @version $Revision: 1.3 $
 */  
#ifndef GC_MF0_H
#define GC_MF0_H


#define GC_STATS
// Define USE_COMPRESSED_VTABLE_POINTERS here to enable compressed vtable
// pointers within objects.
#ifdef _IPF_
#define USE_COMPRESSED_VTABLE_POINTERS
#endif // _IPF_


#include <assert.h>
#include "open/types.h"
//#include "open/gc.h"
//#include "open/vm_gc.h"
//#include "jit_intf.h"

#define LOG_DOMAIN "gc.mf"
#include "cxxlog.h"
/*
#include <iostream>
#undef DIE
#define DIE(x) { std::cerr<<x<<std::endl; std::cerr.flush(); vm_exit(1); }
#undef INFO
#define INFO(x) { std::cerr<<x<<std::endl; std::cerr.flush(); }
*/


struct Object_With_Header;


struct Vtable_GC_Real {
    Class_Handle ch;
    uint16 *offsets;
    const char *name;
    Boolean is_scannable_array: 1;
}; //Vtable_GC 

struct Vtable_GC {
    struct Vtable_GC_Real *gcvt;
}; //Vtable_GC 



struct GC_Overhead {
    Object_With_Header *next;
    unsigned semispace;
    unsigned allocated_size;
    Object_With_Header *forwarding;
#ifdef GC_STATS
    int64 id;
#endif //GC_STATS
};



struct Object_With_Header {
    GC_Overhead header;
#ifdef USE_COMPRESSED_VTABLE_POINTERS
    uint32 vt_offset;
    Vtable_GC *vt() { assert(vt_offset); return (Vtable_GC *) (vt_offset + vm_get_vtable_base()); }
    void *start() { return (void *) &vt_offset; }
    void set_vtable(Allocation_Handle ah) { assert(ah < 1000000); vt_offset = (uint32)ah; }
    static bool use_compressed_vtable_pointers() { return true; }
    static Vtable_GC *allocation_handle_to_vtable(Allocation_Handle ah) {
        return (Vtable_GC *) ((POINTER_SIZE_INT)ah + vm_get_vtable_base());
    }
#else // !USE_COMPRESSED_VTABLE_POINTERS
    Vtable_GC *vt_raw;
    Vtable_GC *vt() { return vt_raw; }
    void *start() { return (void *) &vt_raw; }
    void set_vtable(Allocation_Handle ah) { vt_raw = (Vtable_GC *)ah; }
    static bool use_compressed_vtable_pointers() { return false; }
    static Vtable_GC *allocation_handle_to_vtable(Allocation_Handle ah) {
        return (Vtable_GC *) ah;
    }
#endif // !USE_COMPRESSED_VTABLE_POINTERS
};



struct Root_Set_Entry {
    void **slot;             // Address of a slot containing a pointer to (or into the middle of) an object.
    int offset;              // Base of the object is (*slot - offset), or ((GC::heap_base+*slot) - offset) if a compressed root.
    bool is_compressed_root; // true if this root is compressed.
};



// Raw and compressed reference pointers
#define RAW_REFERENCE         ManagedObject *
#define COMPRESSED_REFERENCE  uint32



class GC {
public:
    static bool verbosegc;
    static bool compressing_references;
    static POINTER_SIZE_INT mx;
    static Object_With_Header *pinned_objects;
    static POINTER_SIZE_INT curr_heap_size;
    static int64 num_collections;
    static bool free_dead_objects;
    static bool zero_dead_objects;
    static int64 start_id_to_collect;
    static int64 end_id_to_collect;

    static POINTER_SIZE_INT heap_base;            // The fictitious base of the collected heap used for compressed references.
    static POINTER_SIZE_INT heap_end;             // The fictitious upper limit of the heap reported in gc_heap_ceiling_address() calls.

    static int64 verify_after_collection; // Turn on root set verification after this collection number.
    static int64 verification_delay;   // After verify_after_collection activates, delay verifications for this many allocations.

    static unsigned current_semispace; // Semispace ID for the current round of allocations.
    static unsigned number_of_semispaces; // Total number of semispaces in the system.
    static Object_With_Header **semispaces;

    // Tracing
    static int64 trace_id;
    static Object_With_Header *trace_obj;
    static int64 trace_collection_number;
    static int64 full_collection_after;

    static bool in_collection;

    static bool doing_verification;
    static bool is_multithreaded;

#ifdef GC_STATS
    static uint64 num_allocated_objects;
#endif //GC_STATS
};



void print_all_objects();
void print_object(Object_With_Header *obj);
void count_totals(int64 *num_objects, int64 *size);
uint64 get_object_size(Object_With_Header *obj);

bool is_marked(Object_With_Header *obj);
void set_marked(Object_With_Header *obj);
void set_unmarked(Object_With_Header *obj);
void unmark_all();
void init_mark_stack();
void reset_mark_stack();

void add_managed_pointer(void *ptr);
void init_ptr_table();
void reset_ptr_table();
void mark_by_pointers();

void mark_all_refs_on_stack();
void mark_recursive(Managed_Object_Handle ref, bool ref_is_compressed);
Object_With_Header *next_object(Object_With_Header *obj);
bool free_unreachable_objects(Object_With_Header **p_semispace, bool subtract_from_heap_size);

void init_moving_component();
void add_managed_pointer_with_base(void **slot, int offset, bool is_compressed_root);
void add_managed_pointer_without_base(void **slot, Boolean is_pinned);
void unmark_all_in_current_semispace();
void mark_by_pointers_in_current_semispace();
void update_roots();
bool move_reachable_objects();
void gather_and_verify_roots();
void validate_object(Object_With_Header *obj);


bool is_compressed_reference(COMPRESSED_REFERENCE value);
bool is_null_compressed_reference(COMPRESSED_REFERENCE value);


void perform_gc(bool forced);

void trace_by_id();

#endif //GC_MF0_H
