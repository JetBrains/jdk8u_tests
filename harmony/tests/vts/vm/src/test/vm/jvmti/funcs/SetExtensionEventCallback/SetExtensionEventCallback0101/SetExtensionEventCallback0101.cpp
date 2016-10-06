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
 * @version $Revision: 1.1 $
 *
 */

/* *********************************************************************** */

#include "events.h"
#include "utils.h"
#include "fake.h"

static bool test = false;
static bool util = false;

const char test_case_name[] = "SetExtensionEventCallback0101";

/* *********************************************************************** */

static void JNICALL myHandler(jvmtiEnv* jvmti_env, jint myInt);

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = {JVMTI_EVENT_VM_DEATH };
    cb_death;
    jvmtiExtensionEvent cb_ptr = (jvmtiExtensionEvent)myHandler;
    jvmtiExtensionEventInfo* ext_events;
    jint counter;
    jvmtiEnv *jvmti;
    jint res = func_for_Agent_OnLoad_JVMTI(vm, options, reserved, &CB,
        events, sizeof(events)/4, test_case_name, DEBUG_OUT, &jvmti);

    jvmtiError result = jvmti->GetExtensionEvents(&counter, &ext_events);
    fprintf(stderr, "\tnative: GetExtensionEvents result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: counter is %d \n", counter);
    fprintf(stderr, "\tnative: ext_events ptr is %p \n", ext_events);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE)  return JNI_ERR;

    util = true;

    if (counter == 0) {
      test =true;
      return JNI_OK;
    }

    result = jvmti->SetExtensionEventCallback(ext_events[0].extension_event_index, cb_ptr);
    fprintf(stderr, "\tnative: SetExtensionEventCallback result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: ext_events[0].extension_event_index is %d \n",
            ext_events[0].extension_event_index);
    fprintf(stderr, "\tnative: cb_ptr ptr is %p \n", cb_ptr);
    fflush(stderr);

    if (result == JVMTI_ERROR_NONE) test = true;

    return JNI_OK;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

void JNICALL myHandler(jvmtiEnv* jvmti_env, jint myInt)
{
    fprintf(stderr, "\tnative myHandler : myInt is %d \n", myInt);
    fprintf(stderr, "\tnative myHandler : jvmti_env is %p \n", jvmti_env);
    fflush(stderr);

    return;
}

/* *********************************************************************** */

