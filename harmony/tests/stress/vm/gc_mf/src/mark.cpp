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
#include <assert.h>

#include "open/types.h"
#include "open/gc.h"
#include "open/vm_gc.h"
#include "jit_intf.h"

#include "gc_mf0.h"




///////////////////////////////////////////////////////////////////////
/// begin set as marked

#define MARKED_MASK   1

bool is_marked(Object_With_Header *obj)
{
    if(MARKED_MASK & (POINTER_SIZE_INT)obj->header.next) {
        return true;
    } else {
        return false;
    }
} //is_marked



void set_marked(Object_With_Header *obj)
{
    obj->header.next = (Object_With_Header *)((POINTER_SIZE_INT)obj->header.next | MARKED_MASK);
} //set_marked



void set_unmarked(Object_With_Header *obj)
{
    obj->header.next = (Object_With_Header *)((POINTER_SIZE_INT)obj->header.next & ~MARKED_MASK);
} //set_unmarked



void unmark_all()
{
    Object_With_Header *obj = GC::pinned_objects;
    while(obj) {
        set_unmarked(obj);
        obj = next_object(obj);
    }
    unmark_all_in_current_semispace();
} //unmark_all


/// end set as marked
///////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////////////////////////////
/// begin mark stack

#define MARK_STACK_SIZE  100000

static Managed_Object_Handle *mark_stack = NULL;
static int mark_stack_ptr = 0;


void init_mark_stack()
{
    mark_stack = (Managed_Object_Handle *)malloc(MARK_STACK_SIZE * sizeof(mark_stack[0]));
    reset_mark_stack();
} //init_mark_stack



void reset_mark_stack()
{
    mark_stack_ptr = 0;
} //reset_mark_stack



static void push_on_mark_stack(Managed_Object_Handle ref)
{
    if(ref == NULL) {
        return;
    }
    if(mark_stack_ptr >= MARK_STACK_SIZE) {
        // Of course, it would be trivial to realloc the mark stack.
        DIE("Fatal GC error: exceeded mark stack");
    }

    // Verify the referenced object.
    Object_With_Header *obj;
    if (GC::compressing_references) {
        Managed_Object_Handle ref2 = (Managed_Object_Handle)(GC::heap_base + (COMPRESSED_REFERENCE)ref);
        obj = (Object_With_Header *)(((Byte *)ref2) - sizeof(GC_Overhead));
    } else {
        obj = (Object_With_Header *)(((Byte *)ref)  - sizeof(GC_Overhead));
    }
    validate_object(obj);

    mark_stack[mark_stack_ptr] = ref;
    mark_stack_ptr++;
} //push_on_mark_stack



static Managed_Object_Handle pop_from_mark_stack()
{
    if(mark_stack_ptr > 0) {
        mark_stack_ptr--;
        return mark_stack[mark_stack_ptr];
    } else {
        return NULL;
    }
} //pop_from_mark_stack



/// end mark stack
///////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////////////////////////////
/// begin mark the heap


void mark_all_refs_on_stack()
{
    while(Managed_Object_Handle ref = pop_from_mark_stack()) {
        mark_recursive(ref, /*is_compressed_root*/ false);
    }
} //mark_all_refs_on_stack



Object_With_Header *next_object(Object_With_Header *obj)
{
    obj = obj->header.next;
    obj  = (Object_With_Header *)((POINTER_SIZE_INT)obj & ~MARKED_MASK);
    return obj;
} //next_object



static void scan_array(Object_With_Header *obj)
{
    Vector_Handle vector = (Vector_Handle)obj->start();
    int32 length = vector_get_length(vector);
    Type_Info_Handle tih = class_get_element_type_info(obj->vt()->gcvt->ch);
    if(type_info_is_reference(tih) ||
       type_info_is_vector(tih) ||
       type_info_is_general_array(tih)) {
        Managed_Object_Handle *first_elem_addr =
            vector_get_element_address_ref(vector, 0);
        if (GC::compressing_references) {
            for(int i = 0; i < length; i++) {
                COMPRESSED_REFERENCE elem = ((COMPRESSED_REFERENCE *)first_elem_addr)[i];
                mark_recursive((void *)elem, /*is_compressed_root*/ true);
            }
        } else {
            for(int i = 0; i < length; i++) {
                mark_recursive(first_elem_addr[i], /*is_compressed_root*/ false);
            }
        }
    } else if(type_info_is_primitive(tih)) {
    } else if(type_info_is_unboxed(tih)) {
        Class_Handle ech = type_info_get_class(tih);
        assert(ech);
        int first_elem_offset = vector_first_element_offset_unboxed(ech);
        int base_offset = 0;//(int)class_get_unboxed_data_offset(ech);
        int elem_size = class_element_size(obj->vt()->gcvt->ch);
        Vtable_GC *evt = (Vtable_GC *)class_get_vtable(ech);
        uint16 *offsets = evt->gcvt->offsets;
        int curr_elem_offset = first_elem_offset;
        for(int i = 0; i < length; i++) {
            uint16 *o = offsets;
            while(*o) {
                int adjusted_offset = curr_elem_offset + *o - base_offset;
                Managed_Object_Handle *slot = (Managed_Object_Handle *)(((Byte *)vector) + adjusted_offset);
                mark_recursive(*slot, /*is_compressed_root*/ GC::compressing_references);
                o++;
            }
            curr_elem_offset += elem_size;
        }
    } else {
        ABORT("All possible cases should already be covered");
    }
} //scan_array

void mark_recursive(Managed_Object_Handle ref, bool ref_is_compressed)
{
    // If ref_is_compressed, ref is a compressed reference: the uint32 offset to an object in the heap.
    // ref_is_compressed => (GC::compressing_references && is_compressed_reference(ref))
    assert(!ref_is_compressed || (GC::compressing_references && is_compressed_reference((COMPRESSED_REFERENCE)ref)));

    uint64 null_ref = (GC::compressing_references? GC::heap_base : NULL);
    if (ref_is_compressed) {
        if (ref == 0) {
            return;
        }
    } else {
        if ((uint64)ref == null_ref) {
            return;
        }
    }

    Object_With_Header *obj;
    if (ref_is_compressed) {
        Managed_Object_Handle ref2 = (Managed_Object_Handle)(GC::heap_base + (COMPRESSED_REFERENCE)ref);
        obj = (Object_With_Header *)(((Byte *)ref2) - sizeof(GC_Overhead));
    } else {
        obj = (Object_With_Header *)(((Byte *)ref)  - sizeof(GC_Overhead));
    }

    unsigned space = obj->header.semispace;
    validate_object(obj);
    if(false && GC::verbosegc) {
#ifdef GC_STATS
        printf("Marking object: %s%p [id=%I64d]\n", (ref_is_compressed? "compressed " : ""), ref, obj->header.id);
#endif //GC_STATS
    }

    if(is_marked(obj)) {
        return;
    }

    set_marked(obj);
    Vtable_GC *vt = obj->vt();
    if(false && GC::verbosegc) {
        printf("\tObject of type %s\n", class_get_name(vt->gcvt->ch));
    }
    if(vt->gcvt->is_scannable_array) {
        scan_array(obj);
    } else {
        uint16 *offsets = vt->gcvt->offsets;
        Managed_Object_Handle ref2 = ref;
        if (ref_is_compressed) {
            ref2 = (Managed_Object_Handle)(GC::heap_base + (COMPRESSED_REFERENCE)ref);
        }
        if (GC::compressing_references) {
            while(*offsets) {
                COMPRESSED_REFERENCE *slot = (COMPRESSED_REFERENCE *)(((Byte *)ref2) + (*offsets));
                COMPRESSED_REFERENCE val_offset = (COMPRESSED_REFERENCE)(*slot);
                assert(is_compressed_reference(val_offset));
                Managed_Object_Handle val = (Managed_Object_Handle)(GC::heap_base + val_offset);
                push_on_mark_stack(val); 
                offsets++;
            }
        } else {
            while(*offsets) {
                Managed_Object_Handle *slot = (Managed_Object_Handle *)(((Byte *)ref2) + *offsets);
                push_on_mark_stack(*slot);
                offsets++;
            }
        }   
    }
} //mark_recursive



/// end mark the heap
///////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////
/// begin managed pointers



#define PTR_TABLE_SIZE  1000

static void **ptr_table = NULL;
static int ptr_table_ptr = 0;

void init_ptr_table()
{
    ptr_table = (void **)malloc(PTR_TABLE_SIZE * sizeof(ptr_table[0]));
    reset_ptr_table();
} //init_ptr_table



void reset_ptr_table()
{
    ptr_table_ptr = 0;
} //reset_ptr_table



void add_managed_pointer(void *ptr)
{
    // Don't add null pointers
    if(ptr == NULL) {
        return;
    }

    // Don't add duplicate entries
    for(int i = 0; i < ptr_table_ptr; i++) {
        if(ptr_table[i] == ptr) {
            return;
        }
    }

    if(ptr_table_ptr >= PTR_TABLE_SIZE) {
        // Of course, it would be trivial to realloc the pointer table.
        DIE("Fatal GC error: exceeded managed pointer table");
    }

    ptr_table[ptr_table_ptr] = ptr;
    ptr_table_ptr++;
} //add_managed_pointer



static void remove_pointer_from_table(int ptr_idx)
{
    int last_idx = ptr_table_ptr - 1;
    assert(last_idx >= ptr_idx);
    for(int i = ptr_idx; i < last_idx; i++) {
        ptr_table[i] = ptr_table[i + 1];
    }
    ptr_table_ptr--;
} //remove_pointer_from_table



static void mark_pointer_to_object(Object_With_Header *obj, int ptr_idx)
{
    if(!is_marked(obj)) {
        // This object would have been deleted, hadn't we handled managed
        // pointers in GC.
        push_on_mark_stack(obj->start());
    }
    remove_pointer_from_table(ptr_idx);
} //mark_pointer_to_object



static void mark_object_by_pointers(Object_With_Header *obj)
{
    uint64 sz = get_object_size(obj);
    Byte *obj_start = (Byte *)obj->start();
    Byte *obj_end = (Byte *)(((Byte *)obj_start) + sz);

    for(int i = 0; i < ptr_table_ptr; i++) {
        Byte *ptr = (Byte *)ptr_table[i];

        // ptr > obj_start: because pointers can't point to a start of a boxed object
        // ptr <= obj_end: to account for pointer just past the last element of an array
        if(ptr > obj_start && ptr <= obj_end) {
            mark_pointer_to_object(obj, i);

            // There are no duplicates in the table.
            break;
        }
    }

    mark_all_refs_on_stack();
} //mark_object_by_pointers



void mark_by_pointers()
{
    if(ptr_table_ptr == 0) {
        return;
    }

    Object_With_Header *obj = (Object_With_Header *) GC::pinned_objects;
    while(obj) {
        mark_object_by_pointers(obj);

        // An optimization: if we used every pointer at least once, we don't
        // have to continue scanning the heap.
        if(ptr_table_ptr == 0) {
            break;
        }

        obj = next_object(obj);
    }
    mark_by_pointers_in_current_semispace();
} //mark_by_pointers





/// end managed pointers
///////////////////////////////////////////////////////////////////////




///////////////////////////////////////////////////////////////////////
/// begin tracing

static void trace_by_addr(void *obj_addr, int distance)
{
    // An arbitrary cut-off for a search that's too deep to be useful.
    if(distance > 20) {
        return;
    }

    Object_With_Header *obj = (Object_With_Header *) GC::pinned_objects;
    while(obj) {
        uint64 sz = get_object_size(obj);
        void **obj_start = (void **)obj->start();
        void **obj_end = (void **)(((Byte *)obj_start) + sz);
        for(void **p = obj_start; p < obj_end; p++) {
            if(obj_addr == *p) {
                printf("GC TRACE: [%d] %p in obj %p(+%d): %s\n",
                       distance,
                       obj_addr,
                       obj_start,
                       (int)(((Byte *)p) - ((Byte *)obj_start)),
                       class_get_name(obj->vt()->gcvt->ch));
                if(!is_marked(obj)) {
                    printf("GC TRACE: %p is not marked\n", obj_start);
                    trace_by_addr(obj_start, distance + 1);
                }
            }
        }
        obj = next_object(obj);
    }
} //trace_by_addr



void trace_by_id()
{
    if(GC::trace_obj == NULL) {
        // No tracing or the object hasn't been allocated yet.
        return;
    }
    if(GC::trace_collection_number >= 0) {
        // Object has been already traced
        return;
    }
    Object_With_Header *obj = GC::trace_obj;
    if(is_marked(obj)) {
        // This GC hasn't reclaimed the object
        return;
    }
    GC::trace_collection_number = GC::num_collections;
    printf("GC TRACE: Tracing unmarked object %p: %s\n", obj->start(), class_get_name(obj->vt()->gcvt->ch));
    void *obj_addr = obj->start();
    trace_by_addr(obj_addr, 0);
} //trace_by_id


/// end tracing
///////////////////////////////////////////////////////////////////////
