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
static bool flag = false;

const char test_case_name[] = "PopFrame0107";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = { JVMTI_EVENT_EXCEPTION, JVMTI_EVENT_VM_DEATH };
    cb_exc;
    cb_death;
    return func_for_Agent_OnLoad(vm, options, reserved, &CB, events,
            sizeof(events)/4, test_case_name, DEBUG_OUT);
}

/* *********************************************************************** */

void JNICALL callbackException(prms_EXCPT)
{
    check_EXCPT;

    if (flag) return;

    char* name;
    char* signature;
    char* generic;
    jint tcount;
    jthread* threads;
    jthread my_thread;
    jvmtiError result;
    jvmtiThreadInfo tinfo;

    /*
     * Function separate all other exceptions in all other method
     */
    if (!check_phase_and_method_debug(jvmti_env, method, SPP_LIVE_ONLY,
                "special_method", DEBUG_OUT))
    {
        if (check_phase_and_method_debug(jvmti_env, method, SPP_LIVE_ONLY,
                    "sub_special", DEBUG_OUT)) test = false;
        return;
    }

    flag = true;
    util = true;

    result = jvmti_env->GetAllThreads(&tcount, &threads);
    fprintf(stderr, "\tnative: GetAllThreads result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return;

    for ( int i = 0; i < tcount; i++ )
        if (jvmti_env->GetThreadInfo(threads[i], &tinfo) == JVMTI_ERROR_NONE)
            if (!strcmp(tinfo.name, "agent"))
            {
                fprintf(stderr, "\tnative: tested thread was found\n");
                my_thread = threads[i];
                break;
            }

    result = jvmti_env->SuspendThread(my_thread);
    fprintf(stderr, "\tnative: SuspendThread result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return;

    result = jvmti_env->PopFrame(create_not_alive_thread(jni_env, jvmti_env));
    fprintf(stderr, "\tnative: PopFrame result = %d (must be JVMTI_ERROR_THREAD_NOT_ALIVE (15)) \n", result);
    fprintf(stderr, "\tnative: thread is %p (must be NON-zero) \n", thread);
    fflush(stderr);
    if (result == JVMTI_ERROR_THREAD_NOT_ALIVE) test = true;

    result = jvmti_env->ResumeThread(my_thread);
    fprintf(stderr, "\tnative: ResumeThread result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */


