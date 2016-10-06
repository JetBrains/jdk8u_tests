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

const char test_case_name[] = "IsArrayClass0101";

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

    char* signature;
    char* generic;
    jint classes_count = 0;
    jclass* classes;
    jvmtiError result;
    jboolean res = false;
    jclass myclass;

    /*
     * Function separate all other exceptions in all other method
     */
    if (!check_phase_and_method_debug(jvmti_env, method, SPP_LIVE_ONLY,
                "special_method", DEBUG_OUT)) return;

    flag = true;
    util = true;

    result = jvmti_env->GetLoadedClasses(&classes_count, &classes);
    fprintf(stderr, "\tnative: GetLoadedClasses result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: classes count is %d \n", classes_count);
    fprintf(stderr, "\tnative: classes ptr is %p \n", classes);
    if (result != JVMTI_ERROR_NONE) return;

    for (int i = 0; i < classes_count; i++)
    {
        result = jvmti_env->GetClassSignature(classes[i], &signature, &generic);
        if (DEBUG_OUT)
        {
            fprintf(stderr, "\tnative: GetClassSignature result = %d (must be zero) \n", result);
            fprintf(stderr, "\tnative: classes ptr is %p \n", classes);
            fprintf(stderr, "\tnative: signature is %s \n", signature);
            fprintf(stderr, "\tnative: generic is %s \n", generic);
            fflush(stderr);
        }

        if (result != JVMTI_ERROR_NONE) continue;

        if (strncmp(signature, "[I", 2)) continue;
        fprintf(stderr, "\tnative: GetClassSignature result = %d (must be zero) \n", result);
        fprintf(stderr, "\tnative: classes ptr is %p \n", classes);
        fprintf(stderr, "\tnative: signature is %s \n", signature);
        fprintf(stderr, "\tnative: generic is %s \n", generic);
        fflush(stderr);

        myclass = classes[i];

        break;
    }

    result = jvmti_env->IsArrayClass( myclass, &res );
    fprintf(stderr, "\tnative: IsInterface result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: class is %p \n", myclass);
    fprintf(stderr, "\tnative: is array: %d \n", res);
    fflush(stderr);

    if (result != JVMTI_ERROR_NONE) return;
    if (res) test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */


