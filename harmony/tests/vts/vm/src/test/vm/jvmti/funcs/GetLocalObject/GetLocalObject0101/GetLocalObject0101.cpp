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

const char test_case_name[] = "GetLocalObject0101";

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
    if (flag) return;

    check_EXCPT;

    jint depth = 0; /* I need current function only */
    jint varnum;
    jint slot = 0;
    jobject value;
    jvmtiError result;
    jvmtiLocalVariableEntry* table;

    /*
     * Function separate all other exceptions in all other method
     */
    if (!check_phase_and_method_debug(jvmti_env, method, SPP_LIVE_ONLY,
                "special_method", DEBUG_OUT)) return;

    flag = true;
    util = true;

    result = jvmti_env->GetLocalVariableTable(method, &varnum, &table);
    fprintf(stderr, "\tnative: GetLocalVariableTable result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: method ID is %p (must be NON-zero) \n", method);
    fprintf(stderr, "\tnative: variables number is %d (must be 6) \n", varnum);
    fprintf(stderr, "\tnative: table is %p (must be NON-zero)\n", table);
    fflush(stderr);

    if (result != JVMTI_ERROR_NONE) return;

    for (int i = 0; i < varnum; i++)
        if (!strcmp(table[i].name, "_OBJ"))
            slot = table[i].slot;

    result = jvmti_env->GetLocalObject(NULL, depth, slot, &value);

    fprintf(stderr, "\tnative: GetLocalObject result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: value is %p (must be 2) \n", value);

    if (result == JVMTI_ERROR_NONE) test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */


