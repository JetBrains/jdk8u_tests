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

static bool flag = false;
static bool test = false;
static bool util = false;

const char test_case_name[] = "SetEventNotificationMode0103";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;
    cb_exc;
    cb_exccatch;
    cb_death;
    jvmtiEvent events[] = { JVMTI_EVENT_EXCEPTION, JVMTI_EVENT_EXCEPTION_CATCH, JVMTI_EVENT_VM_DEATH };

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

    results[4] = jvmti->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_EXCEPTION, NULL);
    fprintf(stderr, "\tnative: SetEventNotificationMode result = %d (must be (0)) \n", results[4]);
    if (results[4] != JVMTI_ERROR_NONE)
    {
        printf("Failure of enabling event: %d\n", (int)results[4]);
        return JNI_ERR;
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

void JNICALL callbackException(prms_EXCPT)
{
    if (flag) return;
    check_EXCPT;
    /*
     * Function separate all other exceptions in all other method
     */
    if (!check_phase_and_method_debug(jvmti_env, method, SPP_LIVE_ONLY,
                "special_method", DEBUG_OUT)) return;

    flag = true;
    util = true;

    jvmtiError result = jvmti_env->SetEventNotificationMode(JVMTI_ENABLE,
            JVMTI_EVENT_EXCEPTION_CATCH, create_not_alive_thread(jni_env, jvmti_env));
    fprintf(stderr, "\tnative: SetEventNotificationMode result = %d (must be JVMTI_ERROR_THREAD_NOT_ALIVE (15)) \n",
            result);
    if (result == JVMTI_ERROR_THREAD_NOT_ALIVE) test = true;

    return;
}

void JNICALL callbackExceptionCatch(prms_EXCPT_CATCH)
{
    check_EXCPT_CATCH;
    return;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */




