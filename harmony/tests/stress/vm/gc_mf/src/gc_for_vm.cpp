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

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <assert.h>


#include "open/types.h"
#include "open/gc.h"
#include "open/vm_gc.h"
#include "open/vm.h"
#include "vm_import.h"
#include "init.h"

#include "platform_lowlevel.h"


// Need for GC_Barriers
#include "open/gc.h"

#include "gc_mf0.h"


#ifdef GC_STATS
uint64 num_allocated_objects = 0;
#endif //GC_STATS



bool GC::verbosegc = false;

bool GC::compressing_references = false;

// The fictitious base and upper limit of the heap. heap_base is used to form raw reference pointers when compressing references.
POINTER_SIZE_INT GC::heap_base = -1;
POINTER_SIZE_INT GC::heap_end  = -1;

POINTER_SIZE_INT GC::mx = 4000000;

Object_With_Header *GC::pinned_objects = NULL;
POINTER_SIZE_INT GC::curr_heap_size = 0;
int64 GC::num_collections = 0;

bool GC::free_dead_objects = true;
bool GC::zero_dead_objects = true;

int64 GC::start_id_to_collect = -1;
int64 GC::end_id_to_collect = -1;

int64 GC::verify_after_collection = 0xFFFFffff;  // a large number to prevent verification by default
int64 GC::verification_delay = 0;

unsigned GC::current_semispace = 1; // 0 is arbitrarily reserved for pinned objects.
unsigned GC::number_of_semispaces = 0;
Object_With_Header **GC::semispaces = NULL;

int64 GC::trace_id = -1;
Object_With_Header *GC::trace_obj = 0;
int64 GC::trace_collection_number = -1;
int64 GC::full_collection_after = -1;

bool GC::in_collection = false;
bool GC::doing_verification = false;
bool GC::is_multithreaded = false;

#ifdef GC_STATS
uint64 GC::num_allocated_objects = 0;
#endif //GC_STATS

static int64 parse_number(const char *str)
{
    char unit_k = 'k';
    char unit_m = 'm';

    size_t len = strlen(str);
    int unit = 1;
    if(tolower(str[len - 1]) == unit_k) {
        unit = 1024;
    } else if(tolower(str[len - 1]) == unit_m) {
        unit = 1024 * 1024;
    }
    int64 result = atoi(str) * unit;
    return result;
} //parse_number

/*
static bool parse_bool_param(bool &var, const char *str, const char *arg)
{
    bool found = true;
    if(strncmp(arg, str, strlen(str)) == 0) {
        const char *value = arg + strlen(str);
        if(strcmp(value, "on") == 0) {
            var = true;
        } else if(strcmp(value, "off") == 0) {
            var = false;
        } else {
            printf("Ignored GC parameter: '%s'\n", arg);
        }
    } else {
        found = false;
    }
    return found;
} //parse_bool_param
*/


static long parse_size_string(const char* size_string) {
    size_t len = strlen(size_string);
    int unit = 1;
    if (tolower(size_string[len - 1]) == 'k') {
        unit = 1024;
    } else if (tolower(size_string[len - 1]) == 'm') {
        unit = 1024 * 1024;
    } else if (tolower(size_string[len - 1]) == 'g') {
        unit = 1024 * 1024 * 1024;
    }
    long size = atol(size_string);
    size *= unit;
    return size;
}

static bool get_property_value_boolean(char* name) {
    const char* value = vm_get_property_value(name);
    return (NULL != value 
        && 0 != value[0]
        && strcmp("0", value) != 0
        && strcmp("off", value) != 0 
        && strcmp("false", value) != 0);
}

static const char* get_property_value(char* name) {
    const char* result = vm_get_property_value(name);
    if (!result || !result[0]) return NULL;
    return result;
}

static void read_configuration_properties()
{

    INFO("gc.mf: reading configuration properties");

    const char* ms = get_property_value("gc.ms");
    if (NULL != ms) {
        if(GC::verbosegc) {
            printf("Initial heap size is ignored\n");
        }
    }
    
    const char* mx = get_property_value("gc.mx");
    if (NULL != mx) {
        GC::mx = parse_size_string(mx);
    }
    INFO("gc.mx = " << GC::mx);

    const char* collect_ids_start = get_property_value("gc.mf.collect_ids_start");
    if (NULL != collect_ids_start) {
        GC::start_id_to_collect = parse_number(collect_ids_start);
    }
    INFO("gc.mf.collect_ids_start = " << GC::start_id_to_collect);

    const char* collect_ids_end = get_property_value("gc.mf.collect_ids_end");
    if (NULL != collect_ids_end) {
        GC::end_id_to_collect = parse_number(collect_ids_end);
    }
    INFO("gc.mf.collect_ids_end = " << GC::end_id_to_collect);

    const char* trace_id = get_property_value("gc.mf.trace_id");
    if (NULL != trace_id) {
        GC::trace_id = parse_number(trace_id);
    }
    INFO("gc.mf.trace_id = " << GC::trace_id);

    GC::free_dead_objects = get_property_value_boolean("gc.mf.free_dead_objects");
    INFO("gc.mf.free_dead_objects = " << GC::free_dead_objects);

    GC::zero_dead_objects = get_property_value_boolean("gc.mf.zero_dead_objects");
    INFO("gc.mf.zero_dead_objects = " << GC::zero_dead_objects);

    GC::verbosegc = get_property_value_boolean("gc.verbose");

    const char* semispaces = get_property_value("gc.mf.semispaces");
    if (NULL != semispaces)
    {
        GC::number_of_semispaces = (unsigned) parse_number(semispaces);
        INFO("gc.mf.semispaces = " << GC::number_of_semispaces);
    }

    const char* verify_after = get_property_value("gc.mf.verify_after");
    if (NULL != verify_after)
    {
        GC::verify_after_collection = (unsigned) parse_number(verify_after);
    }
    INFO("gc.mf.verify_after = " << GC::verify_after_collection);

    const char* verification_delay = get_property_value("gc.mf.verification_delay");
    if (NULL != verification_delay)
    {
        GC::verification_delay = (unsigned) parse_number(verification_delay);
    }
    INFO("gc.mf.verification_delay = " << GC::verification_delay);

    const char* full_collection_after = get_property_value("gc.mf.full_collection_after");
    if (NULL != full_collection_after)
    {
        GC::full_collection_after = (unsigned) parse_number(full_collection_after);
    }
    INFO("gc.mf.full_collection_after = " << GC::full_collection_after);

    if (!get_property_value_boolean("vm.assert_dialog")) {
        disable_assert_dialogs();
    }
}

 

extern "C"
void gc_class_loaded(VTable_Handle vth)
{
} //gc_class_loaded 



extern "C"
void gc_init()
{
    read_configuration_properties();

    GC::compressing_references = (vm_references_are_compressed() ? true : false);
    INFO2("gc", "GC Malloc-Free v0. Max heap size is " << GC::mx << ".  " 
        << (GC::compressing_references? "Compressing" : "Not compressing") << " references");
    bool vm_compresses_vtables = (vm_vtable_pointers_are_compressed() ? true : false);
    if (Object_With_Header::use_compressed_vtable_pointers() != vm_compresses_vtables)
    {
        printf("GC error: mismatch between Object_With_Header::use_compressed_vtable_pointers()=%d\n", Object_With_Header::use_compressed_vtable_pointers());
        printf("          and vm_vtable_pointers_are_compressed()=%d\n", vm_compresses_vtables);
        vm_exit(1);
    }


    // Set the base used to compute compressed (32 bit) references. 
    assert(GC::heap_base == -1);    // else gc_init() wasn't called first.
#ifdef POINTER64
    // ? 20030509 On IPF, the malloc'ed heap often starts at an address above the first 4GB so more than 32 bits are needed to represent
    // references. In particular, compressed references would need more than 32 bits if a heap_base of 0x123 were used. So, use a heap_base
    // close to but (hopefully) below any malloc'ed address.
    int64 early_malloced_ptr = (int64)malloc(10);
    if (early_malloced_ptr >= 0xFFffFFff) {
        GC::heap_base = (early_malloced_ptr & 0xFFffFFff00000000) + 1;
    } else {
        // This might fail...
        GC::heap_base = early_malloced_ptr - 20*1024*1024 - 1;
    }
    // Set heap_end to an address 4GB higher than heap_base (actually, heap_base-1 to avoid having an odd upper bound).
    uint64 four_gigabytes = (uint64)4*1024*1024*1024;
    GC::heap_end  = ((GC::heap_base-1) + four_gigabytes);
#else  // !_IPF_
    // ? 20030313 We use a value in the first 32 bits (4GB) of memory that is different from zero (so compressed  
    // references are different from raw ones), very unlikely to ever be a valid heap address, easily recognized, 
    // and odd (to force an alignment error if used).
    GC::heap_base = 0x123;
    GC::heap_end  = 0xffFFffFF;
#endif // !_IPF_

    GC::in_collection = false;
    init_mark_stack();
    init_ptr_table();
    init_moving_component();
} //gc_init



extern "C"
void gc_vm_initialized()
{
} //gc_vm_initialized

 

extern "C"
void gc_wrapup() 
{
    if(GC::verbosegc) {
        printf("There were %I64d garbage collections\n", GC::num_collections);
#ifdef GC_STATS
        printf("There were %I64d allocated objects\n", GC::num_allocated_objects);
#endif //GC_STATS
    }
} //gc_wrapup



extern "C"
Boolean gc_requires_barriers() 
{
    return FALSE;
} //gc_requires_barriers



// Returns TRUE if references within objects and vector elements are to be treated as offsets rather than raw pointers.
extern "C"
Boolean gc_supports_compressed_references()
{
    return (GC::compressing_references? TRUE : FALSE);
} //gc_supports_compressed_references



extern "C"
void gc_write_barrier(Managed_Object_Handle p_base_of_object_holding_ref) 
{
    ABORT("Not implemented");
} //gc_write_barrier

extern "C"
void *gc_heap_base_address()
{
    // ? 20030313 We use a value in the first 32 bits (4GB) of memory that is different from zero (so compressed  
    // references are different from raw ones), very unlikely to ever be a valid heap address, easily recognized, 
    // and odd (to force an alignment error if used).
    return (void *)GC::heap_base;
} //gc_heap_base_address



extern "C"
void *gc_heap_ceiling_address()
{
    return (void *)GC::heap_end;
} //gc_heap_ceiling_address



extern "C"
void gc_add_root_set_entry_interior_pointer(void **slot, int offset, Boolean is_pinned)
{
    assert(!is_pinned); // fill in later
    char *cref = (char *) *slot - offset;
    add_managed_pointer_with_base(slot, offset, /*is_compressed_root*/ false); // Register it with the moving collector.
    if (!GC::doing_verification)
    {
        mark_recursive((Managed_Object_Handle) cref, /*ref_is_compressed*/ false);
        mark_all_refs_on_stack();
    }

    // XXX- for a moving GC, need to update slot as well
} //gc_add_root_set_entry_interior_pointer


extern "C"
void gc_add_root_set_entry_managed_pointer(void **slot,
                                           Boolean is_pinned)
{
    if(false && GC::verbosegc) {
        printf("*** gc_add_root_set_entry_managed_pointer: %p\n", slot);
    }
    add_managed_pointer_without_base(slot, is_pinned);
    add_managed_pointer(*slot);
} //gc_add_root_set_entry_managed_pointer


extern "C"
void gc_add_root_set_entry(Managed_Object_Handle *ref, Boolean is_pinned)
{
    assert(!is_pinned); // fill in later
    if(false && GC::verbosegc) {
        printf("gc_add_root_set_entry: *(%p)=%p\n", ref, *ref);
    }
    add_managed_pointer_with_base(ref, 0, /*is_compressed_root*/ false); // Register it with the moving collector.
    if (!GC::doing_verification)
    {
        mark_recursive(*ref, /*ref_is_compressed*/ false);
        mark_all_refs_on_stack();
    }
} //gc_add_root_set_entry


bool is_compressed_reference(COMPRESSED_REFERENCE compressed_ref) 
{
    // A compressed reference is an offset into the heap.
    uint64 heap_max_size = (GC::heap_end - GC::heap_base);
    return ((uint64)compressed_ref < heap_max_size);
} //is_compressed_reference



bool is_null_compressed_reference(COMPRESSED_REFERENCE compressed_ref)
{
    // Null compressed references are represented as a zero offset.
    return (compressed_ref == 0); 
} //is_null_compressed_reference



// Resembles gc_add_root_set_entry() but is passed the address of a location containing a compressed reference.
extern "C"
void gc_add_compressed_root_set_entry(uint32 *ref, Boolean is_pinned)
{
    assert(!is_pinned); // fill in later
    assert(GC::compressing_references);
    assert(is_compressed_reference(*ref));
    // Base of the object itself, not the GC header, is at ((GC::heap_base + *ref) - offset)
    if(false && GC::verbosegc) {
        printf("gc_add_compressed_root_set_entry: *(%p)=%p\n", ref, (void*)(*ref));
    }
    add_managed_pointer_with_base((void **)ref, 0, /*is_compressed_root*/ true); // Register it with the moving collector.
    if (!GC::doing_verification)
    {
        mark_recursive((Managed_Object_Handle)*ref, /*ref_is_compressed*/ true);
        mark_all_refs_on_stack();
    }
} //gc_add_compressed_root_set_entry



extern "C"
void gc_thread_init(void *gc_information)
{
    GC::is_multithreaded = true;
} //gc_thread_init



extern "C"
void gc_thread_kill(void *gc_information)
{
} //gc_thread_kill



extern "C"
Boolean gc_is_object_pinned(Managed_Object_Handle obj)
{
    if (GC::number_of_semispaces > 0)
        return FALSE;
    else
        return TRUE;
} //gc_is_object_pinned



extern "C"
unsigned int gc_time_since_last_gc()
{
    ABORT("Not implemented");
    return 0;
} //gc_time_since_last_gc



const char *get_module_kind()
{
    return "GC";
} //get_module_kind



const char *get_module_name()
{
    return "GC MF";
} //get_module_name



const char *get_module_version()
{
    return "2003-02-05";
} //get_module_version



const char *get_required_interface_version()
{
    return "2003-02";
} //get_required_interface_version





extern "C" void gc_test_safepoint()
{
    if (!GC::in_collection && GC::full_collection_after >= 0 && GC::num_allocated_objects >= (uint64)GC::full_collection_after)
    {
        perform_gc(false);
    }
}

//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

/// Comments
//
// 2. What does the following line to in gc_header.h?
//    extern unsigned vm_array_size(Class *vector_class, int length);
//
// 3. Change 'array' to 'vector' in many interface function names.
//
// 4. Need
//    VMEXPORT void *vector_get_element_address_unboxed(Vector_Handle vector, int32 idx);
//    for arrays of structs
//
// 6. Can we get rid of the following?
//      VMEXPORT enum safepoint_state get_global_safepoint_status();
//
// 9. Given
//      p_thread_lock = p_gc_lock = new Lock_Manager();
//    and the use of p_thread_lock in Java_java_lang_Thread_sleep_generic:
//      p_thread_lock->_lock_enum(); 
//      assert(p_thr->app_status == thread_is_sleeping);
//    we can't grab p_gc_lock with the vm_gc_lock_enum_or_null in gc_alloc
//    or gc_force_gc.  What can the GC use to satisfy its synchronization?
//
// 11. Why are the following still VMEXPORT?
//       VMEXPORT void _lock_enum();
//       VMEXPORT void _unlock_enum();
//       VMEXPORT bool _lock_or_null();
//       VMEXPORT void _unlock_or_null();
//       VMEXPORT void _unlock_enum_or_null ();
//       VMEXPORT bool _lock_enum_or_null (bool return_null_on_fail);
//
