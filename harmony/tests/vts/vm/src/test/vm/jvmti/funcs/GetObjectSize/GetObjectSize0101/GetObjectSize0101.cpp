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

const char test_case_name[] = "GetObjectSize0101";

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

    jint depth = 0;
    jint varnum = 0;
    jvmtiLocalVariableEntry* table;
    jvmtiError result;
    jobject object_a = NULL;
    jobject object_b = NULL;
    jlong size_a = 0;
    jlong size_b = 0;
    jint slot_a = 0;
    jint slot_b = 0;

    /*
     * Function separate all other exceptions in all other method
     */
    if (!check_phase_and_method_debug(jvmti_env, method, SPP_LIVE_ONLY,
                "special_method", DEBUG_OUT)) return;

    flag = true;

    result = jvmti_env->GetLocalVariableTable(method, &varnum, &table);
    fprintf(stderr, "\tnative: GetLocalVariableTable result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: method ID is %p (must be NON-zero) \n", method);
    fprintf(stderr, "\tnative: variables number is %d (must be 6) \n", varnum);
    fprintf(stderr, "\tnative: table is %p (must be NON-zero)\n", table);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) return;

    for (int i = 0; i < varnum; i++)
    {
        if (!strcmp(table[i].name, "obj_a"))
            slot_a = table[i].slot;
        if (!strcmp(table[i].name, "obj_b"))
            slot_b = table[i].slot;
    }

    result = jvmti_env->GetLocalObject(NULL, depth, slot_a, &object_a);
    fprintf(stderr, "\tnative: GetLocalObject A result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: value_a_ptr is %p \n", object_a);
    if (result != JVMTI_ERROR_NONE) return;

    result = jvmti_env->GetLocalObject(NULL, depth, slot_b, &object_b);
    fprintf(stderr, "\tnative: GetLocalObject B result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: value_b_ptr is %p \n", object_b);
    if (result != JVMTI_ERROR_NONE) return;

    util = true;

    jvmtiError result1 = jvmti_env->GetObjectSize(object_a, &size_a);
    fprintf(stderr, "\tnative: GetObjectSize #1 result = %d (must be zero) \n", result1);
    fprintf(stderr, "\tnative: object A is %p \n", object_a);
    fprintf(stderr, "\tnative: size_a is %lld \n", (long long)size_a);
    fflush(stderr);

    jvmtiError result2 = jvmti_env->GetObjectSize(object_b, &size_b);
    fprintf(stderr, "\tnative: GetObjectSize #1 result = %d (must be zero) \n", result2);
    fprintf(stderr, "\tnative: fobject B is %p \n", object_b);
    fprintf(stderr, "\tnative: size_b is %lld \n", (long long)size_b);
    fflush(stderr);


    if ((result1 == JVMTI_ERROR_NONE) &&
            (result2 == JVMTI_ERROR_NONE) &&
            (size_a < size_b)) test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */


