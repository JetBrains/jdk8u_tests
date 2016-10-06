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
static bool flag = false;

const char test_case_name[] = "GetAvailableProcessors0101";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = { JVMTI_EVENT_EXCEPTION, JVMTI_EVENT_VM_DEATH };
    cb_exc;
    cb_death;
    return func_for_Agent_OnLoad(vm, options, reserved, &CB,
        events, sizeof(events)/4, test_case_name, DEBUG_OUT);
}

/* *********************************************************************** */

void JNICALL callbackException(prms_EXCPT)
{
    check_EXCPT;
    if (flag) return;
    jvmtiError result;
    jint procesor_number_ti = 0;
    jint procesor_number_os = 0;

    /*
     * Function separates all exceptions in all other method
     */
    if (!check_phase_and_method_debug(jvmti_env, method, SPP_LIVE_ONLY,
                "special_method", DEBUG_OUT)) return;

    flag = true;
    util = true;

    /*
     * Getting of number of available processors by operation system
     * tools ...
     */
#ifdef LINUX /* on Linux */
    procesor_number_os = (int)sysconf(_SC_NPROCESSORS_CONF);
#else /* on Windows */
    SYSTEM_INFO system_info;
    GetSystemInfo(&system_info);
    procesor_number_os = system_info.dwNumberOfProcessors;
#endif

    result = jvmti_env->GetAvailableProcessors(&procesor_number_ti);
    fprintf(stderr, "\tnative: GetAvailableProcessors result = %d (must be zero) \n", result);
    if (result != JVMTI_ERROR_NONE) return;
    fprintf(stderr, "\tnative: procesor_number_ti    is %d \n", procesor_number_ti);
    fprintf(stderr, "\tnative: procesor_number_os    is %d \n", procesor_number_os);
    fflush(stderr);
    if (procesor_number_os == procesor_number_ti) test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */



