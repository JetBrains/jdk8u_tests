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
#include "fake.h"

static bool test = false;
static bool util = false;

const char test_case_name[] = "SetSystemProperty0101";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = {JVMTI_EVENT_VM_DEATH };
    cb_death;
    jvmtiEnv *jvmti;
    jvmtiError result;
    jint res = func_for_Agent_OnLoad_JVMTI(vm, options, reserved, &CB,
        events, sizeof(events)/4, test_case_name, DEBUG_OUT, &jvmti);

    char* value;
    jint prop_counter = 0;
    char** props;
    int ii = 0;
    jint count = 0;

    util = true;

    result = jvmti->SetSystemProperty("java.bla_bla_bla.property", "bla-bla-bla_0");
    fprintf(stderr, "\tnative: SetSystemProperty 1 result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return JNI_OK;

    result = jvmti->SetSystemProperty("java.bla_bla_bla.property", "bla-bla-bla_1");
    fprintf(stderr, "\tnative: SetSystemProperty 2 result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return JNI_OK;

    result = jvmti->GetSystemProperties(&count,  &props);
    fprintf(stderr, "\tnative: GetSystemProperties result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return JNI_OK;

    for ( ii = 0; ii < count; ii++ )
    {
        fprintf(stderr, "\tnative: property [%d]     is %s \n", ii, props[ii]);
        if (strcmp(props[ii], "java.bla_bla_bla.property")) continue;
        prop_counter++;
        result = jvmti->GetSystemProperty(props[ii], &value);
        if (result != JVMTI_ERROR_NONE) return JNI_OK;
        fprintf(stderr, "\t\tnative: property value    is %s \n", value);
        fprintf(stderr, "\t\tnative: property count    is %d \n", prop_counter);

        if (!strcmp(value, "bla-bla-bla_1"))
        {
            if (prop_counter == 1)
                test = true;
            else
                test = false;
        }
    }

    fflush(stderr);

    return JNI_OK;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

