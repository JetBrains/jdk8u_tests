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

#include "open/types.h"
#include "open/gc.h"
#include "open/vm_gc.h"
#include "jit_intf.h"

#include "gc_mf0.h"



void print_object(Object_With_Header *obj)
{
    Vtable_GC *vt = obj->vt();
    Class_Handle ch = vt->gcvt->ch;
    const char *name = class_get_name(ch);
#ifdef GC_STATS
    printf("print_object: %p, id %d, start %p, type %s\n", obj, (int)obj->header.id, obj->start(), name);
#else  // !GC_STATS
    printf("print_object: %p, start %p, type %s\n", obj, obj->start(), name);
#endif // !GC_STATS
} //print_object



void print_all_objects()
{
    LOG("=== Start print_all_objects");
    Object_With_Header *obj = GC::pinned_objects;
    while(obj) {
        print_object(obj);
        obj = next_object(obj);
    }
    if (GC::number_of_semispaces > 0)
    {
        obj = GC::semispaces[GC::current_semispace % GC::number_of_semispaces];
        while(obj) {
            print_object(obj);
            obj = next_object(obj);
        }
    }
    LOG("=== End print_all_objects\n");
} //print_all_objects



void count_totals(int64 *num_objects, int64 *size)
{
    int64 num = 0;
    int64 sz = 0;
    Object_With_Header *obj = GC::pinned_objects;
    while(obj) {
        num++;
        sz += get_object_size(obj);
        obj = next_object(obj);
    }
    if (GC::number_of_semispaces > 0)
    {
        obj = GC::semispaces[GC::current_semispace % GC::number_of_semispaces];
        while(obj) {
            num++;
            sz += get_object_size(obj);
            obj = next_object(obj);
        }
    }
    *num_objects = num;
    *size = sz;
} //count_totals



extern "C"
int64 gc_total_memory() 
{
    int64 num_objects = 0;
    int64 size = 0;
    count_totals(&num_objects, &size);
    return size;
} //gc_total_memory



extern "C"
int64 gc_free_memory() 
{
    return GC::mx - gc_total_memory();
} //gc_free_memory
