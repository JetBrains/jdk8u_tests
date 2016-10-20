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
#include <string.h>

#include "open/types.h"
#include "open/gc.h"
#include "open/vm_gc.h"
#include "jit_intf.h"


#include "gc_mf0.h"




static void get_roots_and_mark()
{
    reset_mark_stack();
    reset_ptr_table();
    vm_enumerate_root_set_all_threads();
    mark_all_refs_on_stack();
    mark_by_pointers();
} //get_roots_and_mark



static bool id_in_range_to_collect(Object_With_Header *obj)
{
#ifdef GC_STATS
    int64 id = obj->header.id;
    if(GC::start_id_to_collect < 0 || GC::start_id_to_collect <= id) {
        if(GC::end_id_to_collect < 0 || id <= GC::end_id_to_collect) {
            return true;
        }
    }
#endif // GC_STATS
    return false;
} //id_in_range_to_collect



static void free_object(Object_With_Header *obj, bool subtract_from_heap_size)
{
    uint64 sz = get_object_size(obj);
    if (subtract_from_heap_size)
        GC::curr_heap_size -= sz;
    if(false && GC::verbosegc) {
        printf("FREEING: ");
        print_object(obj);
    }

#ifdef GC_STATS
    if(obj->header.id == GC::trace_id) {
        printf("GC TRACE: Object %p, id %d of type %s is reclaimed\n",
               obj->start(), (int)obj->header.id,
               class_get_name(obj->vt()->gcvt->ch));
    }
#endif // GC_STATS

    if(!id_in_range_to_collect(obj)) {
        return;
    }

    if(GC::free_dead_objects) {
        free(obj);
    } else if(GC::zero_dead_objects) {
        // The cast below may be unsafe for objects > 2^32 bytes
        memset(obj->start(), 0, (size_t)sz);
    }
} //free_object



// The p_semispace argument can point to GC::pinned_objects or to GC::semispace[i].
bool free_unreachable_objects(Object_With_Header **p_semispace, bool subtract_from_heap_size)
{
    bool success = false;
    // Initially ignore the first element to simplify the logic in the loop
    Object_With_Header *obj = *p_semispace;
    while(obj) {
        Object_With_Header *next = next_object(obj);
        if(next) {
            if(!is_marked(next)) {
                bool obj_is_marked = is_marked(obj);
                obj->header.next = next_object(next);
                if(obj_is_marked) {
                    set_marked(obj);
                }
                free_object(next, subtract_from_heap_size);
                success = true;
            } else {
                obj = next_object(obj);
            }
        } else {
            break;
        }
    }

    // Now handle the first element in the list
    if(*p_semispace && !is_marked(*p_semispace)) {
        Object_With_Header *obj = *p_semispace;
        *p_semispace = next_object(*p_semispace);
        free_object(obj, subtract_from_heap_size);
        success = true;
    }
    return success;
}



// Return true if at least one object was freed.
static bool free_unreachable_pinned_objects()
{
    return free_unreachable_objects(&GC::pinned_objects, true);
} //free_unreachable_pinned_objects



void perform_gc(bool forced)
{
    assert(!GC::in_collection);
    GC::in_collection = true;
    int64 total_num_objects_before = 0;
    int64 total_size_before = 0;
    if(GC::verbosegc) {
        const char *forced_string = forced ? "forced" : "capacity";
        count_totals(&total_num_objects_before, &total_size_before);
        INFO2("gc", "================= Start GC #" << GC::num_collections << " (" << forced_string << "): " 
            << total_num_objects_before << " objects in the heap ("
            << total_size_before << "B), used heap size "
            << GC::curr_heap_size << "B, semispace " << GC::current_semispace);
    }

    get_roots_and_mark();

    if(false && GC::verbosegc) {
        print_all_objects();
    }

    trace_by_id();

    bool success = free_unreachable_pinned_objects();
    success = move_reachable_objects() || success;

    unmark_all();

    update_roots();

    GC::in_collection = false;

    vm_resume_threads_after();

    int64 total_num_objects_after = 0;
    int64 total_size_after = 0;
    if(GC::verbosegc) {
        count_totals(&total_num_objects_after, &total_size_after);
        INFO2("gc", "================= End GC: "
            << total_num_objects_after << " objects live ("
            << total_size_after << "B), reclaimed "
            << (total_num_objects_before - total_num_objects_after) 
            << " (" << (total_size_before - total_size_after) << "B), "
            << "used heap size " << GC::curr_heap_size);
        if (false && GC::verbosegc && !success) {
            printf("***Warning: no objects reclaimed\n");
        }
    }

    GC::num_collections++;
} //perform_gc



extern "C"
void gc_force_gc() 
{
    TRACE("gc_force_gc()");

    vm_gc_lock_enum();   // ---------v

    perform_gc(true);

    vm_gc_unlock_enum(); // ---------^
} //gc_force_gc
