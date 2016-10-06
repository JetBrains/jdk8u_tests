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
 * @author Valentin Al. Sitnick
 * @version $Revision: 1.2 $
 *
 */

/* *********************************************************************** */

#include "events.h"
#include "utils.h"

static bool flag = false;
static bool test = false;
static bool util = false;

const char test_case_name[] = "SetEventNotificationMode0105";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;
    cb_death;
    jvmtiEvent events[] = {JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */
    fflush(stderr);

    util = true;

    fprintf(stderr, "\n----------------------------------------------------");
    fprintf(stderr, "\ntest");
    fprintf(stderr, " %s ", test_case_name);
    fprintf(stderr, "is started\n{\n");
    fflush(stderr);

    /* set event callbacks */
    results[3] = jvmti->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_VM_DEATH, NULL);
    fprintf(stderr, "\tnative: SetEventNotificationMode result = %d (must be (0)) \n", results[3]);
    if (results[3] != JVMTI_ERROR_NONE)
    {
        printf("Failure of enabling event: %d\n", (int)results[3]);\
        return JNI_ERR;
    }

    jvmtiEvent test_events[] = { JVMTI_EVENT_FIELD_MODIFICATION,
        JVMTI_EVENT_FIELD_ACCESS, JVMTI_EVENT_SINGLE_STEP,
        JVMTI_EVENT_EXCEPTION, JVMTI_EVENT_EXCEPTION_CATCH,
        JVMTI_EVENT_FRAME_POP, JVMTI_EVENT_BREAKPOINT,
        JVMTI_EVENT_METHOD_ENTRY, JVMTI_EVENT_METHOD_EXIT,
        JVMTI_EVENT_COMPILED_METHOD_LOAD, JVMTI_EVENT_COMPILED_METHOD_UNLOAD,
        JVMTI_EVENT_MONITOR_CONTENDED_ENTER,
        JVMTI_EVENT_MONITOR_CONTENDED_ENTERED, JVMTI_EVENT_MONITOR_WAIT,
        JVMTI_EVENT_MONITOR_WAITED, JVMTI_EVENT_VM_OBJECT_ALLOC,
        JVMTI_EVENT_NATIVE_METHOD_BIND, JVMTI_EVENT_GARBAGE_COLLECTION_START,
        JVMTI_EVENT_GARBAGE_COLLECTION_FINISH, JVMTI_EVENT_OBJECT_FREE };

    /*
     * Turn off all needed capabilities
     */
    {
        jvmtiCapabilities zero_caps;
        jvmtiError result;
        make_caps_zero(&zero_caps);
        zero_caps.can_generate_field_modification_events = 1;
        zero_caps.can_generate_field_access_events = 1;
        zero_caps.can_generate_single_step_events = 1;
        zero_caps.can_generate_exception_events = 1;
        zero_caps.can_generate_frame_pop_events = 1;
        zero_caps.can_generate_breakpoint_events = 1;
        zero_caps.can_generate_method_entry_events = 1;
        zero_caps.can_generate_method_exit_events = 1;
        zero_caps.can_generate_compiled_method_load_events = 1;
        zero_caps.can_generate_monitor_events = 1;
        zero_caps.can_generate_vm_object_alloc_events = 1;
        zero_caps.can_generate_native_method_bind_events = 1;
        zero_caps.can_generate_garbage_collection_events = 1;
        zero_caps.can_generate_object_free_events = 1;

        result = jvmti->RelinquishCapabilities(&zero_caps);
        if (result != JVMTI_ERROR_NONE)
        {
            fprintf(stderr,
                    "Error during RelinquishCapabilities. Test Stopped\n");
            fflush(stderr);
            return JNI_ERR;
        }
    }

    test = true;

    for (int ii = 0 ; ii < 20; ii++)
    {
        jvmtiError result = jvmti->SetEventNotificationMode(JVMTI_ENABLE, test_events[ii], NULL);
        fprintf(stderr, "\tnative: SetEventNotificationMode result = %d (must be JVMTI_ERROR_MUST_POSSESS_CAPABILITY (99)) \n", result);
        if (result != JVMTI_ERROR_MUST_POSSESS_CAPABILITY)
        {
            fprintf(stderr, "\tnative: Event %d was enabled successfully with error %d\n", (int)test_events[ii], result);
            test = false;
        }
        else
            fprintf(stderr, "\tnative: Expected error for event %d. Error is %d\n", (int)test_events[ii], result);
    }

    /* set event callbacks */
    results[64] = jvmti->SetEventCallbacks( &cb, sizeof(cb));
    if (results[64] != JVMTI_ERROR_NONE)
    {
        printf("Failure of setting event callbacks: %d\n",results[64]);
        return JNI_ERR;
    }

    return JNI_OK;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */





