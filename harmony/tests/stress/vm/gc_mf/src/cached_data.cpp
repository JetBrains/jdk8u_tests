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
#include <string.h>


#include "open/types.h"
#include "open/gc.h"
#include "open/vm_gc.h"
#include "jit_intf.h"


#include "gc_mf0.h"


static int num_cached_ref_fields(VTable_Handle vth)
{
    Vtable_GC *vt = (Vtable_GC *)vth;
    int num = 0;
    uint16 *p = vt->gcvt->offsets;
    while(*p++) {
        num++;
    }
    return num;
} //num_cached_ref_fields



static int num_super_ref_fields(Class_Handle ch)
{
    Class_Handle sch = class_get_super_class(ch);
    if(!sch) {
        return 0;
    } else {
        return num_cached_ref_fields(class_get_vtable(sch));
    }
} //num_super_ref_fields



static int num_ref_fields_recursive(Class_Handle ch);

// We don't handle fields of non-primitive value types.
static int num_ref_fields_no_super(Class_Handle ch)
{
    assert(ch);
    unsigned nf = class_num_instance_fields(ch);
    int nrf = 0;
    for(unsigned idx = 0; idx < nf; idx++) {
        Field_Handle fh = class_get_instance_field(ch, idx);
        Type_Info_Handle tih = field_get_type_info_of_field_value(fh);
        if(type_info_is_reference(tih) ||
           type_info_is_vector(tih) ||
           type_info_is_general_array(tih)) {
            nrf++;
        } else if(type_info_is_primitive(tih) ||
                  type_info_is_unmanaged_pointer(tih)) {
            // nothing to do for these types
        } else if(type_info_is_unboxed(tih)) {
            Class_Handle fch = type_info_get_class(tih);
            assert(fch);
            int nrf_field = num_ref_fields_recursive(fch);
            nrf += nrf_field;
        } else {
            // Other types should be ignored but we should have an
            // explicit test for them like type_info_is_primitive.
            ABORT("All possible cases are expected to be already covered");
        }
    }
    return nrf;
} //num_ref_fields_no_super



static void copy_super_offsets(uint16 *dst, Class_Handle ch)
{
    Class_Handle sch = class_get_super_class(ch);
    if(sch) {
        Vtable_GC *svt = (Vtable_GC *)class_get_vtable(sch);
        uint16 *src = svt->gcvt->offsets;
        while(*dst++ = *src++) {
        }
    }
} //copy_super_offsets



static void copy_offsets_no_super(uint16 *dst, Class_Handle ch)
{
    unsigned nf = class_num_instance_fields(ch);
    for(unsigned idx = 0; idx < nf; idx++) {
        Field_Handle fh = class_get_instance_field(ch, idx);
        Type_Info_Handle tih = field_get_type_info_of_field_value(fh);
        if(type_info_is_reference(tih) ||
           type_info_is_vector(tih) ||
           type_info_is_general_array(tih)) {
            *dst++ = field_get_offset(fh);
        } else if(type_info_is_primitive(tih) ||
                  type_info_is_unmanaged_pointer(tih)) {
            // nothing to do for these types
        } else if(type_info_is_unboxed(tih)) {
            Class_Handle fch = type_info_get_class(tih);
            assert(fch);
            int base_offset = field_get_offset(fh);
            //base_offset -= class_get_unboxed_data_offset(fch);
            Vtable_GC *fvt = (Vtable_GC *)class_get_vtable(fch);
            uint16 *src = fvt->gcvt->offsets;
            while(*src) {
                *dst = *src + base_offset;
                dst++;
                src++;
            }
        } else {
            // Other types should be ignored but we should have an
            // explicit test for them like type_info_is_primitive.
            ABORT("All possible cases are expected to be already covered");
        }
    }
} //copy_offsets_no_super



static void set_scannable_array_property(Class_Handle ch, Vtable_GC *vt)
{
    vt->gcvt->is_scannable_array = FALSE;
    if(!class_is_array(ch)) {
        return;
    }
    Type_Info_Handle tih = class_get_element_type_info(ch);
    if(type_info_is_reference(tih) ||
       type_info_is_vector(tih) ||
       type_info_is_general_array(tih)) {
        vt->gcvt->is_scannable_array = TRUE;
    } else if(type_info_is_primitive(tih) ||
              type_info_is_unmanaged_pointer(tih)) {
    } else if(type_info_is_unboxed(tih)) {
        Class_Handle ech = type_info_get_class(tih);
        assert(ech);
        Vtable_GC *evt = (Vtable_GC *)class_get_vtable(ech);
        if(*evt->gcvt->offsets) {
            vt->gcvt->is_scannable_array = TRUE;
        }
    } else {
        ABORT("All possible cases are expected to be already covered");
    }
} //set_scannable_array_property



static int num_ref_fields_recursive(Class_Handle ch)
{
    int nsrf = num_super_ref_fields(ch);
    int nrf = num_ref_fields_no_super(ch);
    return nsrf + nrf;
} //num_ref_fields_recursive



extern "C"
void gc_class_prepared(Class_Handle ch, VTable_Handle vth)
{
    assert(ch);
    assert(vth);
    Vtable_GC *vt = (Vtable_GC *)vth;
    vt->gcvt = (struct Vtable_GC_Real *) malloc(sizeof(*vt->gcvt));
    vt->gcvt->ch = ch;
    vt->gcvt->name = class_get_name(ch);
    TRACE("gc_class_prepared: " << class_get_name(ch));
    int num_super_rf   = num_super_ref_fields(ch);
    int num_ref_fields = num_ref_fields_recursive(ch);
    assert(num_ref_fields >= num_super_rf);

    uint16 *offsets = (uint16 *)malloc((num_ref_fields + 1) * sizeof(uint16));
    copy_super_offsets(offsets, ch);
    copy_offsets_no_super(offsets + num_super_rf, ch);
    offsets[num_ref_fields] = 0;
    vt->gcvt->offsets = offsets;
    if(false && GC::verbosegc) {
        while(*offsets) {
            printf(" %d", *offsets);
            offsets++;
        }
        printf("\n");
    }

    set_scannable_array_property(ch, vt);
} //gc_class_prepared



uint64 get_object_size(Object_With_Header *obj)
{
    Class_Handle ch = obj->vt()->gcvt->ch;
    if(class_is_array(ch)) {
        int length_offset = vector_length_offset();
        Byte *length_addres = ((Byte *)obj->start()) + length_offset;
        int32 length = *(int32 *)length_addres;
        return vm_vector_size(ch, length);
    } else {
        return class_get_boxed_data_size(ch);
    }
} //get_object_size

