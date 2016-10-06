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

const char test_case_name[] = "GenerateEvents0101";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = { JVMTI_EVENT_EXCEPTION, JVMTI_EVENT_COMPILED_METHOD_LOAD, JVMTI_EVENT_DYNAMIC_CODE_GENERATED, JVMTI_EVENT_VM_DEATH };

    cb_exc;
    cb_cmload;
    cb_codegen;
    cb_death;

    return func_for_Agent_OnLoad(vm, options, reserved, &CB,
        events, sizeof(events)/4, test_case_name, DEBUG_OUT);
}

/* *********************************************************************** */

void JNICALL callbackException(prms_EXCPT)
{
    check_EXCPT;

    if (flag) return;

    char* name;
    char* signature;
    char* generic;
    jvmtiError result;

    /*
     * Function separate all other exceptions in all other method
     */
    if (!check_phase_and_method_debug(jvmti_env, method, SPP_LIVE_ONLY,
                "special_method", DEBUG_OUT)) return;

    flag = true;
    util = true;

    jvmtiError result_1 =
        jvmti_env->GenerateEvents(JVMTI_EVENT_COMPILED_METHOD_LOAD);
    fprintf(stderr, "\tnative: GenerateEvents result = %d (must be zero) \n", result_1);
    fprintf(stderr, "\tnative: JVMTI_EVENT_COMPILED_METHOD_LOAD \n");

    jvmtiError result_2 =
        jvmti_env->GenerateEvents(JVMTI_EVENT_DYNAMIC_CODE_GENERATED);
    fprintf(stderr, "\tnative: GenerateEvents result = %d (must be zero) \n", result_2);
    fprintf(stderr, "\tnative: JVMTI_EVENT_DYNAMIC_CODE_GENERATED \n");

    if ( (result_1 == JVMTI_ERROR_NONE) && (result_2 == JVMTI_ERROR_NONE) )
        test = true;
}

void JNICALL callbackCompiledMethodLoad(prms_COMPL_MET_LD)
{
    check_COMPL_MET_LD;
    //TBD
    //fprintf(stderr, "\tnative CALLBACK: JVMTI_EVENT_COMPILED_METHOD_LOAD \n");
    //fflush(stderr);
}

void JNICALL callbackDynamicCodeGenerated(prms_DYN_CODE_GEN)
{
    check_DYN_CODE_GEN;
    //TBD
    //fprintf(stderr, "\tnative CALLBACK: JVMTI_EVENT_DYNAMIC_CODE_GENERATED \n");
    //fflush(stderr);
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

