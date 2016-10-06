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

static bool test = false;
static bool util = false;

const char test_case_name[] = "SetEventCallbacks0101";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;
    cb_death;
    jvmtiEvent events[] = { JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */
    fflush(stderr);

    util = true;

    fprintf(stderr, "\n----------------------------------------------------");
    fprintf(stderr, "\ntest");
    fprintf(stderr, " %s ", test_case_name);
    fprintf(stderr, "is started\n{\n");
    fflush(stderr);

    results[3] = jvmti->SetEventNotificationMode(JVMTI_ENABLE,
            JVMTI_EVENT_VM_DEATH, NULL);
    fprintf(stderr, "\tnative: SetEventNotificationMode result = %d (must be zero) \n", results[3]);

    if (results[3] != JVMTI_ERROR_NONE)
    {
        printf("Failure of enabling event: %d\n", (int)results[3]);\
        return JNI_ERR;
    }

    /* set event callbacks */
    results[64] = jvmti->SetEventCallbacks( &cb, sizeof(cb));
    fprintf(stderr, "\tnative: SetEventCallbacks result = %d (must be zero) \n", results[64]);
    if (results[64] != JVMTI_ERROR_NONE)
    {
        printf("Failure of setting event callbacks: %d\n",results[64]);
        return JNI_ERR;
    }
    else
        test = true;

    return JNI_OK;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */


