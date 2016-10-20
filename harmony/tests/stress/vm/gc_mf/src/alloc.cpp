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
#include <assert.h>

#include "platform_lowlevel.h" // for CRITICAL_SECTION


#include "open/types.h"
#include "open/gc.h"
#include "open/vm_gc.h"
#include "open/hythread_ext.h"
#include "jit_intf.h"


#include "gc_mf0.h"


static bool ensure_memory(int64 size, Allocation_Handle ah)
{
    int64 heap_plus_size = GC::curr_heap_size + size;
    if(heap_plus_size >= GC::mx ||
        (GC::full_collection_after >= 0 && GC::num_allocated_objects >= (uint64)GC::full_collection_after)) {
        perform_gc(false);
    }
    return (GC::curr_heap_size + size) < GC::mx;
} //ensure_memory



#define BITS_PER_BYTE 8
#define NEXT_TO_HIGH_BIT_SET_MASK (1<<((sizeof(unsigned) * BITS_PER_BYTE)-2))
#define NEXT_TO_HIGH_BIT_CLEAR_MASK ~NEXT_TO_HIGH_BIT_SET_MASK

static unsigned get_instance_data_size(unsigned encoded_size) 
{
    return (encoded_size & NEXT_TO_HIGH_BIT_CLEAR_MASK);
} //get_instance_data_size



static Object_With_Header *alloc_obj_with_header(unsigned sz, bool is_pinned, Allocation_Handle ah)
{
    TRACE("alloc_obj_with_header: sz=0x" << (void *)sz); // (void*) instead of std::hex

    static hymutex_t cs;
    static bool initialized = false;
    if (!initialized)
    {
        initialized = true;
        assert(TM_ERROR_NONE == hymutex_create(&cs, TM_MUTEX_DEFAULT));
    }
    bool multithreaded = GC::is_multithreaded;
    if (multithreaded) {
        assert(TM_ERROR_NONE == hymutex_lock(cs));
    }

    if (GC::num_collections > GC::verify_after_collection)
    {
        if (GC::verification_delay == 0)
        {
            gather_and_verify_roots();
        }
        else
        {
            GC::verification_delay --;
        }
    }

    // Perform GC if needed
    bool success = ensure_memory(sz, ah);
    if(!success) {
        if (multithreaded) {
            assert(TM_ERROR_NONE == hymutex_unlock(cs));
        }
        return 0;
    }

    Object_With_Header **objects;
    if (is_pinned || GC::number_of_semispaces == 0)
        objects = &GC::pinned_objects;
    else
        objects = &GC::semispaces[GC::current_semispace % GC::number_of_semispaces];

    size_t size_with_overhead = sz + sizeof(GC_Overhead);
    Object_With_Header *obj = (Object_With_Header *)malloc(size_with_overhead);
    assert(obj);
#ifdef _IPF_
    // ? 20030509 Make sure that the heap_base is actually lower than the newly allocated address
    // Don't use an assert: we want this to fail in the release build too.
    if ((int64)obj <= GC::heap_base) {
        DIE("GC_MF Failure: In alloc_obj_with_header, "
            << "the newly allocated object at 0x" << (void*) obj
            << " has an address BELOW the heap_base " << GC::heap_base);
    }
#endif // _IPF_

    memset(obj, 0, size_with_overhead);
    obj->header.next = *objects;
    obj->header.forwarding = NULL;
    obj->header.semispace = (is_pinned ? 0 : GC::current_semispace);
    obj->header.allocated_size = (unsigned) size_with_overhead;
    *objects = obj;
    GC::curr_heap_size += sz;
#ifdef GC_STATS
    GC::num_allocated_objects++;
    obj->header.id = GC::num_allocated_objects;
#endif //GC_STATS

    if (multithreaded) {
        assert(TM_ERROR_NONE == hymutex_unlock(cs));
    }
    return obj;
} //alloc_obj_with_header



extern "C"
Managed_Object_Handle gc_alloc_fast(unsigned size, 
                                    Allocation_Handle ah,
                                    void *thread_pointer)
{
    return 0;
} //gc_alloc_fast



////////////////////////////////////////////////////////////
// Not part of the interface
VMEXPORT Class_Handle vm_get_stack_frame(unsigned depth);


extern "C"
Managed_Object_Handle gc_alloc(unsigned size, Allocation_Handle ah, void *thread_pointer)
{
    Vtable_GC *vt = Object_With_Header::allocation_handle_to_vtable(ah);
    size_t sz = get_instance_data_size(size);
    bool is_pinned = (sz != size); // Are there bits in the "size" parameter that indicate pinning???

    // ? 20030404 DEBUG - display info about allocated object
#ifdef GC_STATS
#    define GC_STATS_NUM_ALLOCATED_OBJECTD_MESSAGE "[" << GC::num_allocated_objects << "] " <<
#endif //GC_STATS

    TRACE(GC_STATS_NUM_ALLOCATED_OBJECTD_MESSAGE 
        "gc_alloc: " << class_get_name(vt->gcvt->ch) 
        << " size=0x" << (void*)size << ", sz=0x" << (void*)sz);

    vm_gc_lock_enum();    // ---------v

    Object_With_Header *obj = alloc_obj_with_header((unsigned)sz, is_pinned, ah);

    if(!obj) {
        vm_gc_unlock_enum();
        return NULL;
    }

    obj->set_vtable(ah);
    assert(class_is_array(vt->gcvt->ch) || sz == get_object_size(obj)
        || get_object_size(obj) == 0 // ? 2003-03-10: it seems that get_object_size() may be 0 at bootstrap time.
        );

    vm_gc_unlock_enum(); // ----------^

#ifdef GC_STATS
    if(obj->header.id == GC::trace_id) {
        printf("GC TRACE: Object %p, id %d of type %s is allocated\n",
               obj->start(), (int)obj->header.id,
               class_get_name(obj->vt()->gcvt->ch));
        GC::trace_obj = obj;
    }
#endif //GC_STATS

    // ? 20030404 DEBUG - display info about allocated object
    if(false && GC::verbosegc) {
#ifdef GC_STATS
        printf("[%I64d]", GC::num_allocated_objects);
        if (is_pinned) {
            printf("gc_alloc: 0x%x, id=%d, size=%u, PINNED, %s\n",
                   obj->start(), (int)obj->header.id, size, class_get_name(vt->gcvt->ch));
        } else {
            printf("gc_alloc: 0x%x, id=%u, size=%d, %s\n",
                   obj->start(), (int)obj->header.id, size, class_get_name(vt->gcvt->ch));
        } 
#else  // !GC_STATS
        if (is_pinned) {
            printf("gc_alloc: 0x%x, size=%u, %s\n",
                   obj->start(), size, PINNED, class_get_name(vt->gcvt->ch));
        } else {
            printf("gc_alloc: 0x%x, size=%u, %s\n",
                   obj->start(), size, class_get_name(vt->gcvt->ch));
        }
#endif // !GC_STATS
    }

    return obj->start(); 
} //gc_alloc




extern "C"
Managed_Object_Handle gc_pinned_malloc_noclass(unsigned size) 
{
    TRACE("gc_pinned_malloc_noclass (size = 0x" << (void*)size << ")");

    vm_gc_lock_enum();   // ---------v

    Object_With_Header *obj = alloc_obj_with_header(size, true, 0);
    assert(obj);

    vm_gc_unlock_enum(); // ---------^

    return obj->start(); 
} //gc_pinned_malloc_noclass
