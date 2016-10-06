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

const char test_case_name[] = "IsFieldSynthetic0105";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = { JVMTI_EVENT_CLASS_PREPARE, JVMTI_EVENT_VM_DEATH };
    cb_clprep;
    cb_death;
    return func_for_Agent_OnLoad(vm, options, reserved, &CB, events,
            sizeof(events)/4, test_case_name, DEBUG_OUT);
}

/* *********************************************************************** */

void JNICALL callbackClassPrepare(prms_CLS_PRPR)
{
    check_CLS_PRPR;

    if (flag) return;

    char* name;
    char* signature;
    char* generic;
    jint fld_num;
    jfieldID* fids = NULL;
    jfieldID my_fid = NULL;
    jboolean is_synthetic;
    jvmtiError result;

    result = jvmti_env->GetClassSignature(klass, &signature,&generic);
    if (result != JVMTI_ERROR_NONE) return;
    if (strcmp(signature, "Lorg/apache/harmony/vts/test/vm/jvmti/IsFieldSynthetic0105;"))
        return;
    fprintf(stderr, "\tnative: GetClassSignature result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: klass ptr is %p \n", klass);
    fprintf(stderr, "\tnative: signature is %s \n", signature);
    fprintf(stderr, "\tnative: generic is %s \n", generic);
    fflush(stderr);

    result = jvmti_env->GetClassFields(klass, &fld_num, &fids);
    fprintf(stderr, "\tnative: GetClassFields result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: klass ptr is %p \n", klass);
    fprintf(stderr, "\tnative: fld_num is %d \n", fld_num);
    fprintf(stderr, "\tnative: fids is ptr %p \n", fids);
    fflush(stderr);
    if (result != JVMTI_ERROR_NONE) return;

    flag = true;
    util = true;

    for (int i = 0; i < fld_num; i++)
    {
        result = jvmti_env->GetFieldName(klass, fids[i], &name, &signature, &generic);
        if (DEBUG_OUT)
        {
            fprintf(stderr, "\tnative: GetClassFields result = %d (must be zero) \n", result);
            fprintf(stderr, "\tnative: klass ptr is %p \n", klass);
            fprintf(stderr, "\tnative: fids[%d] is %p \n", i, fids[i]);
            fprintf(stderr, "\tnative: name is %s \n", name);
            fprintf(stderr, "\tnative: signature is %s \n", signature);
            fprintf(stderr, "\tnative: generic is %s \n", generic);
            fflush(stderr);
        }
        if (result !=  JVMTI_ERROR_NONE) continue;
        if (strcmp(name, "special_field_for_test")) continue;

        fprintf(stderr, "\tnative: GetClassFields result = %d (must be zero) \n", result);
        fprintf(stderr, "\tnative: klass ptr is %p \n", klass);
        fprintf(stderr, "\tnative: fids[%d] is %p \n", i, fids[i]);
        fprintf(stderr, "\tnative: name is %s \n", name);
        fprintf(stderr, "\tnative: signature is %s \n", signature);
        fprintf(stderr, "\tnative: generic is %s \n", generic);
        fflush(stderr);

        my_fid = fids[i];
        break;
    }

    result = jvmti_env->IsFieldSynthetic(klass, my_fid, NULL);
    fprintf(stderr, "\tnative: IsFieldSynthetic result = %d (must be JVMTI_ERROR_NULL_POINTER (100)) \n", result);
    fflush(stderr);
    if (result == JVMTI_ERROR_NULL_POINTER) test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

