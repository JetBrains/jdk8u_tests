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
#include <malloc.h>
#include <string.h>
#include <assert.h>

#include "open/types.h"
#include "open/gc.h"
#include "open/vm_gc.h"
#include "jit_intf.h"

#include "gc_mf0.h"

#define MAX_ROOT_SET_ENTRIES 10000

static Root_Set_Entry roots[MAX_ROOT_SET_ENTRIES];
static unsigned num_roots;

void init_moving_component()
{
    if (GC::number_of_semispaces == 0)
        return;

    if (GC::number_of_semispaces <= 1)
    {
        WARN("GC: Number of semispaces must be at least 2.  Using fixed collector.");
        GC::number_of_semispaces = 0;
        return;
    }
    
    GC::semispaces = (Object_With_Header **) malloc((size_t) (GC::number_of_semispaces * sizeof(*GC::semispaces)));
    for (unsigned i=0; i<GC::number_of_semispaces; i++)
    {
        GC::semispaces[i] = NULL;
    }

    num_roots = 0;

    INFO("Moving GC enabled with " << GC::number_of_semispaces << " semispaces.");
}

void validate_object(Object_With_Header *obj)
{
    Vtable_GC *vt = obj->vt();
    assert(vt);
    unsigned semispace = obj->header.semispace;
    if (semispace != 0 && semispace != GC::current_semispace)
    {
#ifdef GC_STATS
        WARN("GC validation failure, obj=0x" << (void*) obj->start() << ", "
            << "type=" << vt->gcvt->name << ", id=" << (int) obj->header.id << ", "
            << "last created in semispace=" << semispace << ", current semispace=" << GC::current_semispace);
#else  // !GC_STATS
        printf("GC validation failure, obj=0x%p, type=%s, last created in semispace=%d, current semispace=%d\n",
            obj->start(), vt->gcvt->name, semispace, GC::current_semispace);
#endif // !GC_STATS
    }
    assert(semispace == 0 || semispace == GC::current_semispace);
}

void add_managed_pointer_with_base(void **slot, int offset, bool is_compressed_slot)
{
    assert(!is_compressed_slot || (GC::compressing_references && is_compressed_reference( *((COMPRESSED_REFERENCE *)slot) )));
    if (GC::number_of_semispaces == 0)
        return;
    if ((!is_compressed_slot && *slot == NULL) ||
        (is_compressed_slot && *(COMPRESSED_REFERENCE *)slot == NULL))
        return;
    Object_With_Header *obj;
    if (is_compressed_slot) {
        COMPRESSED_REFERENCE *compressed_slot = (COMPRESSED_REFERENCE *)slot;
        COMPRESSED_REFERENCE obj_offset = (COMPRESSED_REFERENCE)(*compressed_slot);
        obj = (Object_With_Header *) ((Byte *) (GC::heap_base + obj_offset) - offset - sizeof(GC_Overhead));
    } else {
        obj = (Object_With_Header *) ((Byte *) *slot - offset - sizeof(GC_Overhead));
    }
    validate_object(obj);

#ifdef GC_STATS
    if(obj->header.id == GC::trace_id) {
        printf("GC TRACE: Obj %p, id %d, type %s enum during %s\n",
               obj->start(), (int)obj->header.id,
               class_get_name(obj->vt()->gcvt->ch),
               (GC::doing_verification? "verify" : "GC"));
    }
#endif // GC_STATS

    assert(num_roots < MAX_ROOT_SET_ENTRIES);
    roots[num_roots].slot = slot;
    roots[num_roots].offset = offset;
    roots[num_roots].is_compressed_root = is_compressed_slot;
    num_roots ++;
}

void add_managed_pointer_without_base(void **slot, Boolean is_pinned)
{
    ABORT("Not implemented");
}

static void update_root(void **slot, int offset)
{
    if (*slot == NULL)
        return;
    Byte *vtable = ((Byte *) *slot) - offset;
    Object_With_Header *obj = (Object_With_Header *) (vtable - sizeof(GC_Overhead));
    assert(obj);
    obj = obj->header.forwarding;
    if (obj != NULL)
    {
        assert(GC::number_of_semispaces >= 2);
        Byte *new_slot = ((Byte *) obj->start()) + offset;
        *slot = (void *) new_slot;
    }
}

// Like update_root() but is passed the address of a location containing a compressed reference: the uint32 heap offset to/into the referenced object
static void update_compressed_root(COMPRESSED_REFERENCE *slot, int offset)
{
    assert(GC::compressing_references);
    assert(is_compressed_reference(*slot));
    if (*slot == 0)
        return;
    Byte *vtable = ((Byte *) (GC::heap_base + *slot)) - offset;
    Object_With_Header *obj = (Object_With_Header *) (vtable - sizeof(GC_Overhead));
    assert(obj);
    obj = obj->header.forwarding;
    if (obj != NULL)
    {
        assert(GC::number_of_semispaces >= 2);
        Byte *new_slot = ((Byte *) obj->start()) + offset;
        if (new_slot != NULL) {
            COMPRESSED_REFERENCE new_slot_offset = (COMPRESSED_REFERENCE)((uint64)new_slot - (uint64)GC::heap_base);
            assert(is_compressed_reference(new_slot_offset));
            *((COMPRESSED_REFERENCE *)slot) = new_slot_offset;
        } else {
            *((COMPRESSED_REFERENCE *)slot) = 0;
        }
    }
}

void update_roots()
{
    for (unsigned i=0; i<num_roots; i++)
    {
        void **slot = roots[i].slot;
        int offset = roots[i].offset;
        if (roots[i].is_compressed_root) {
            update_compressed_root((COMPRESSED_REFERENCE *)slot, offset);
        } else {
            update_root(slot, offset);
        }
    }

    num_roots = 0; // Prepare for the next root set enumeration.
}

void unmark_all_in_current_semispace()
{
    if (GC::number_of_semispaces == 0)
        return;
    Object_With_Header *obj = GC::semispaces[GC::current_semispace % GC::number_of_semispaces];
    while(obj) {
        set_unmarked(obj);
        obj = next_object(obj);
    }
    GC::current_semispace ++;
}

void mark_by_pointers_in_current_semispace()
{
    if (GC::number_of_semispaces == 0)
        return;
    ABORT("Not implemented");
}

static void update_pointers_in_array(Object_With_Header *obj)
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
            for (int i = 0; i < length; i++) {
                update_compressed_root((COMPRESSED_REFERENCE *)&first_elem_addr[i], 0);
            }
        } else {
            for (int i = 0; i < length; i++) {
                update_root(&first_elem_addr[i], 0);
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
                if (GC::compressing_references) {
                    update_compressed_root((COMPRESSED_REFERENCE *)slot, 0);
                } else {
                    update_root(slot, 0);
                }
                o++;
            }
            curr_elem_offset += elem_size;
        }
    } else {
        ABORT("All possible cases should be already covered");
    }
} //update_pointers_in_array

static void update_pointers_in_semispace(Object_With_Header *semispace, bool is_pinned_semispace)
{
    Object_With_Header *obj;
    for (obj=semispace; obj!=NULL; obj=next_object(obj))
    {
        assert(obj->header.semispace == 0 || !is_marked(obj));
        if (is_pinned_semispace && !is_marked(obj))
            continue;
        Vtable_GC *vt = obj->vt();
        if (vt->gcvt->is_scannable_array)
        {
            update_pointers_in_array(obj);
        }
        else
        {
            uint16 *offsets = vt->gcvt->offsets;
            while(*offsets) {
                void **slot = (void **)(((Byte *)obj->start()) + *offsets);
                if (GC::compressing_references) {
                    update_compressed_root((COMPRESSED_REFERENCE *)slot, 0);
                } else {
                    update_root(slot, 0);
                }
                offsets++;
            }
        }
    }
}

bool move_reachable_objects()
{
    bool result = false;
    if (GC::number_of_semispaces == 0)
        return result;
    unsigned current_semispace = GC::current_semispace % GC::number_of_semispaces;
    unsigned target_semispace = (GC::current_semispace + 1) % GC::number_of_semispaces;
    // Free the old objects in the target semispace.
    result = (GC::semispaces[target_semispace] != NULL);
    free_unreachable_objects(&GC::semispaces[target_semispace], false);
    assert(GC::semispaces[target_semispace] == NULL);

    // Move live objects from the current semispace into the target semispace.
    Object_With_Header *obj;
    for (obj=GC::semispaces[current_semispace]; obj!=NULL; obj=next_object(obj))
    {
        if (is_marked(obj))
        {
            Object_With_Header *new_obj = (Object_With_Header *) malloc(obj->header.allocated_size);
#ifdef _IPF_
            // ? 20030509 Make sure that the heap_base is actually lower than the newly allocated address
            // Don't use an assert: we want this to fail in the release build too.
            if ((int64)new_obj <= GC::heap_base) {
                printf("GC_MF Failure: In move_reachable_objects, the newly allocated object at 0x%I64x has an address BELOW the heap_base 0x%I64x\n", 
                       (int64)obj, GC::heap_base);
                exit(-1);
            }
#endif // _IPF_
            memcpy(new_obj->start(), obj->start(), obj->header.allocated_size - sizeof(GC_Overhead));
            new_obj->header.allocated_size = obj->header.allocated_size;
            new_obj->header.forwarding = NULL;
            obj->header.forwarding = new_obj;
#ifdef GC_STATS
            new_obj->header.id = obj->header.id;
#endif // GC_STATS
            new_obj->header.semispace = GC::current_semispace + 1;
            new_obj->header.next = GC::semispaces[target_semispace];
            GC::semispaces[target_semispace] = new_obj;
        }
        else
        {
            GC::curr_heap_size -= obj->header.allocated_size - sizeof(GC_Overhead);
            result = true;
        }
    }

    // Update pointer fields in the target semispace.
    update_pointers_in_semispace(GC::semispaces[target_semispace], false);

    // Update pointer fields 
    update_pointers_in_semispace(GC::pinned_objects, true);

    return result;
}

void gather_and_verify_roots()
{
    GC::doing_verification = true;
    vm_enumerate_root_set_all_threads();
    for (unsigned i=0; i<num_roots; i++)
    {
        void **slot = roots[i].slot;
        int offset = roots[i].offset;
        if (roots[i].is_compressed_root) {
            // slot is the address of a location containing the compressed reference (a heap offset) for the i-th root.
            assert(GC::compressing_references);
            COMPRESSED_REFERENCE *compressed_slot = (COMPRESSED_REFERENCE *)slot;
            COMPRESSED_REFERENCE ref = *compressed_slot;
            assert(is_compressed_reference(ref));
            if (ref != 0) {
                Object_With_Header *obj = (Object_With_Header *) ((Byte *) (GC::heap_base + ref) - offset - sizeof(GC_Overhead));
                validate_object(obj);
            }
        } else {
            if (*slot != NULL) {
                Object_With_Header *obj = (Object_With_Header *) ((Byte *) *slot - offset - sizeof(GC_Overhead));
                validate_object(obj);
            }
        }
    }
    num_roots = 0;
    GC::doing_verification = false;
    vm_resume_threads_after();
}
