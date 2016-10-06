/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Valentin Al. Sitnick, Gleb Tonkin
 * @version $Revision: 1.2 $
 *
 */

/* *********************************************************************** */

#include "events.h"
#include "utils.h"
#include "fake.h"

static bool test = false;
static bool util = false;

const char test_case_name[] = "RelinquishCapabilities0101";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = {JVMTI_EVENT_VM_DEATH };
    cb_death;
    jvmtiEnv *jvmti;
    jvmtiError result;
    jvmtiCapabilities    curr_caps;
    jvmtiCapabilities    zero_caps;
    jvmtiCapabilities    full_caps;
    jvmtiCapabilities    potential_caps;
    jint cmp1 = 0;
    jint cmp2 = 0;
    jint res = func_for_Agent_OnLoad_JVMTI(vm, options, reserved, &CB,
        events, sizeof(events)/4, test_case_name, DEBUG_OUT, &jvmti);


    result = jvmti->GetPotentialCapabilities(&potential_caps);	
   
    if(result != JVMTI_ERROR_NONE) return JNI_ERR;

    
    fprintf(stderr, "\tnative: make ZERO caps <--- ");
    fflush(stderr);
    make_caps_zero(&zero_caps);
    fprintf(stderr, "finish --->\n");
    fflush(stderr);

    if(potential_caps.can_tag_objects){
		zero_caps.can_tag_objects = 1;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_tag_objects is %d\n", curr_caps.can_tag_objects);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_tag_objects;
	
		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_tag_objects is %d\n", curr_caps.can_tag_objects);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_tag_objects;
    }
    else if(potential_caps.can_generate_field_modification_events){
		zero_caps.can_generate_field_modification_events = 1;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_field_modification_events is %d\n", curr_caps.can_generate_field_modification_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_field_modification_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_field_modification_events is %d\n", curr_caps.can_generate_field_modification_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_field_modification_events;

    }
    else if(potential_caps.can_generate_field_access_events){
	    zero_caps.can_generate_field_access_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_field_access_events is %d\n", curr_caps.can_generate_field_access_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_field_access_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_field_access_events is %d\n", curr_caps.can_generate_field_access_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_field_access_events;

    }
    else if(potential_caps.can_get_bytecodes){
	    zero_caps.can_get_bytecodes = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_bytecodes is %d\n", curr_caps.can_get_bytecodes);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_get_bytecodes;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_bytecodes is %d\n", curr_caps.can_get_bytecodes);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_get_bytecodes;

    }
    else if(potential_caps.can_get_synthetic_attribute){
	    zero_caps.can_get_synthetic_attribute = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_synthetic_attribute is %d\n", curr_caps.can_get_synthetic_attribute);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_get_synthetic_attribute;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_synthetic_attribute is %d\n", curr_caps.can_get_synthetic_attribute);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_get_synthetic_attribute;

    }
    else if(potential_caps.can_get_owned_monitor_info){
	    zero_caps.can_get_owned_monitor_info = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_owned_monitor_info is %d\n", curr_caps.can_get_owned_monitor_info);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_get_owned_monitor_info;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_owned_monitor_info is %d\n", curr_caps.can_get_owned_monitor_info);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_get_owned_monitor_info;

    }
    else if(potential_caps.can_get_current_contended_monitor){
	    zero_caps.can_get_current_contended_monitor = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_current_contended_monitor is %d\n", curr_caps.can_get_current_contended_monitor);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_get_current_contended_monitor;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_current_contended_monitor is %d\n", curr_caps.can_get_current_contended_monitor);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_get_current_contended_monitor;

    }
    else if(potential_caps.can_get_monitor_info){
	    zero_caps.can_get_monitor_info = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_monitor_info is %d\n", curr_caps.can_get_monitor_info);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_get_monitor_info;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_monitor_info is %d\n", curr_caps.can_get_monitor_info);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_get_monitor_info;

    }
    else if(potential_caps.can_pop_frame){
	    zero_caps.can_pop_frame = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_pop_frame is %d\n", curr_caps.can_pop_frame);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_pop_frame;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_pop_frame is %d\n", curr_caps.can_pop_frame);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_pop_frame;

    }
    else if(potential_caps.can_redefine_classes){
	    zero_caps.can_redefine_classes = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_redefine_classes is %d\n", curr_caps.can_redefine_classes);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_redefine_classes;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_redefine_classes is %d\n", curr_caps.can_redefine_classes);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_redefine_classes;

    }
    else if(potential_caps.can_signal_thread){
	    zero_caps.can_signal_thread = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_signal_thread is %d\n", curr_caps.can_signal_thread);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_signal_thread;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_signal_thread is %d\n", curr_caps.can_signal_thread);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_signal_thread;

    }
    else if(potential_caps.can_get_source_file_name){
	    zero_caps.can_get_source_file_name = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_source_file_name is %d\n", curr_caps.can_get_source_file_name);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_get_source_file_name;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_source_file_name is %d\n", curr_caps.can_get_source_file_name);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_get_source_file_name;

    }
    else if(potential_caps.can_get_line_numbers){
	    zero_caps.can_get_line_numbers = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_line_numbers is %d\n", curr_caps.can_get_line_numbers);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_get_line_numbers;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_line_numbers is %d\n", curr_caps.can_get_line_numbers);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_get_line_numbers;

    }
    else if (potential_caps.can_get_source_debug_extension){
	    zero_caps.can_get_source_debug_extension = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_source_debug_extension is %d\n", curr_caps.can_get_source_debug_extension);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_get_source_debug_extension;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_source_debug_extension is %d\n", curr_caps.can_get_source_debug_extension);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_get_source_debug_extension;

    }
    else if(potential_caps.can_access_local_variables){
	    zero_caps.can_access_local_variables = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_access_local_variables is %d\n", curr_caps.can_access_local_variables);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_access_local_variables;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_access_local_variables is %d\n", curr_caps.can_access_local_variables);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_access_local_variables;

    }
    else if(potential_caps.can_maintain_original_method_order){
	    zero_caps.can_maintain_original_method_order = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_maintain_original_method_order is %d\n", curr_caps.can_maintain_original_method_order);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_maintain_original_method_order;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_maintain_original_method_order is %d\n", curr_caps.can_maintain_original_method_order);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_maintain_original_method_order;

    }
    else if(potential_caps.can_generate_single_step_events){
	    zero_caps.can_generate_single_step_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_single_step_events is %d\n", curr_caps.can_generate_single_step_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_single_step_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_single_step_events is %d\n", curr_caps.can_generate_single_step_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_single_step_events;

    }
    else if(potential_caps.can_generate_exception_events){
	    zero_caps.can_generate_exception_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_exception_events is %d\n", curr_caps.can_generate_exception_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_exception_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_exception_events is %d\n", curr_caps.can_generate_exception_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_exception_events;

    }
    else if(potential_caps.can_generate_frame_pop_events){
	    zero_caps.can_generate_frame_pop_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_frame_pop_events is %d\n", curr_caps.can_generate_frame_pop_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_frame_pop_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_frame_pop_events is %d\n", curr_caps.can_generate_frame_pop_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_frame_pop_events;

    }
    else if(potential_caps.can_generate_breakpoint_events){
	    zero_caps.can_generate_breakpoint_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_breakpoint_events is %d\n", curr_caps.can_generate_breakpoint_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_breakpoint_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_breakpoint_events is %d\n", curr_caps.can_generate_breakpoint_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_breakpoint_events;

    }
    else if(potential_caps.can_suspend){
	    zero_caps.can_suspend = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_suspend is %d\n", curr_caps.can_suspend);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_suspend;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_suspend is %d\n", curr_caps.can_suspend);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_suspend;

    }
    else if(potential_caps.can_redefine_any_class){
	    zero_caps.can_redefine_any_class = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_redefine_any_class is %d\n", curr_caps.can_redefine_any_class);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_redefine_any_class;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_redefine_any_class is %d\n", curr_caps.can_redefine_any_class);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_redefine_any_class;

    }
    else if(potential_caps.can_get_current_thread_cpu_time){
	    zero_caps.can_get_current_thread_cpu_time = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_current_thread_cpu_time is %d\n", curr_caps.can_get_current_thread_cpu_time);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_get_current_thread_cpu_time;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_current_thread_cpu_time is %d\n", curr_caps.can_get_current_thread_cpu_time);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_get_current_thread_cpu_time;

    }
    else if(potential_caps.can_get_thread_cpu_time){
	    zero_caps.can_get_thread_cpu_time = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_thread_cpu_time is %d\n", curr_caps.can_get_thread_cpu_time);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_get_thread_cpu_time;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_get_thread_cpu_time is %d\n", curr_caps.can_get_thread_cpu_time);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_get_thread_cpu_time;

    }
    else if(potential_caps.can_generate_method_entry_events){
	    zero_caps.can_generate_method_entry_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_method_entry_events is %d\n", curr_caps.can_generate_method_entry_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_method_entry_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_method_entry_events is %d\n", curr_caps.can_generate_method_entry_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_method_entry_events;

    }
    else if(potential_caps.can_generate_method_exit_events){
	    zero_caps.can_generate_method_exit_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_method_exit_events is %d\n", curr_caps.can_generate_method_exit_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_method_exit_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_method_exit_events is %d\n", curr_caps.can_generate_method_exit_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_method_exit_events;

    }
    else if(potential_caps.can_generate_all_class_hook_events){
	    zero_caps.can_generate_all_class_hook_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_all_class_hook_events is %d\n", curr_caps.can_generate_all_class_hook_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_all_class_hook_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_all_class_hook_events is %d\n", curr_caps.can_generate_all_class_hook_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_all_class_hook_events;

    }
    else if(potential_caps.can_generate_compiled_method_load_events){
		zero_caps.can_generate_compiled_method_load_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_compiled_method_load_events is %d\n", curr_caps.can_generate_compiled_method_load_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_compiled_method_load_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_compiled_method_load_events is %d\n", curr_caps.can_generate_compiled_method_load_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_compiled_method_load_events;

    }
    else if(potential_caps.can_generate_monitor_events){
	    zero_caps.can_generate_monitor_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_monitor_events is %d\n", curr_caps.can_generate_monitor_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_monitor_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_monitor_events is %d\n", curr_caps.can_generate_monitor_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_monitor_events;

    }
    else if(potential_caps.can_generate_vm_object_alloc_events){
	    zero_caps.can_generate_vm_object_alloc_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_vm_object_alloc_events is %d\n", curr_caps.can_generate_vm_object_alloc_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_field_modification_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_vm_object_alloc_events is %d\n", curr_caps.can_generate_vm_object_alloc_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_vm_object_alloc_events;

    }
    else if(potential_caps.can_generate_native_method_bind_events){
	    zero_caps.can_generate_native_method_bind_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_native_method_bind_events is %d\n", curr_caps.can_generate_native_method_bind_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_native_method_bind_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_native_method_bind_events is %d\n", curr_caps.can_generate_native_method_bind_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_native_method_bind_events;

    }
    else if(potential_caps.can_generate_garbage_collection_events){
	    zero_caps.can_generate_garbage_collection_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_garbage_collection_events is %d\n", curr_caps.can_generate_garbage_collection_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_garbage_collection_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_garbage_collection_events is %d\n", curr_caps.can_generate_garbage_collection_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_garbage_collection_events;

    }
    else if(potential_caps.can_generate_object_free_events){
	    zero_caps.can_generate_object_free_events = 1;
		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_object_free_events is %d\n", curr_caps.can_generate_object_free_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp1 = curr_caps.can_generate_object_free_events;

		result = jvmti->RelinquishCapabilities(&zero_caps);
		fprintf(stderr, "\tnative: RelinquishCapabilities result = %d (must be zero) \n", result);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		result = jvmti->GetCapabilities(&curr_caps);
		fprintf(stderr, "\tnative: GetCapabilities result = %d (must be zero) \n", result);
		fprintf(stderr, "\tnative: curr_caps ptr is %p\n", &curr_caps);
		fprintf(stderr, "\tnative: curr_caps.can_generate_object_free_events is %d\n", curr_caps.can_generate_object_free_events);
		fflush(stderr);
		if (result != JVMTI_ERROR_NONE) return JNI_ERR;

		cmp2 = curr_caps.can_generate_object_free_events;

    }
    else {
    	test = false;
		fprintf(stderr, "GetPotencialCapabilities return: No capabilities is 1\n");
		fflush(stderr);

		return res;
    }

    if (cmp1 != cmp2) test = true;

    util = true;
    return res;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

