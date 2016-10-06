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

const char test_case_name[] = "SetFieldModificationWatch0101";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = { JVMTI_EVENT_EXCEPTION, JVMTI_EVENT_VM_DEATH, JVMTI_EVENT_FIELD_MODIFICATION };
    cb_exc;
    cb_mod;
    cb_death;
    return func_for_Agent_OnLoad(vm, options, reserved, &CB, events,
            sizeof(events)/4, test_case_name, DEBUG_OUT);
}

/* *********************************************************************** */

void JNICALL callbackException(prms_EXCPT)
{
    check_EXCPT;

    if (flag) return;

    jvmtiError result;
    jclass myclass = NULL;
    jfieldID myfield = NULL;

    /*
     * Function separate all other exceptions in all other method
     */
    if (!check_phase_and_method_debug(jvmti_env, method, SPP_LIVE_ONLY,
                "special_method", DEBUG_OUT)) return;

    flag = true;
    util = true;

    if (!is_needed_field_found(jvmti_env, "SetFieldModificationWatch0101.java", "third_field", &myclass, &myfield, DEBUG_OUT))
        return;

    result = jvmti_env->SetFieldModificationWatch(myclass, myfield);
    fprintf(stderr, "\tnative: SetFieldModificationWatch result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: class is %p \n", myclass);
    fprintf(stderr, "\tnative: field is %p \n", myfield);
    fflush(stderr);
}

void JNICALL callbackFieldModification(prms_FLD_MODIF)
{
    check_FLD_MODIF;

    test = true;
    fprintf(stderr, "\tnative: FieldModification event was received\n");
    fprintf(stderr, "\tnative: JVMTI Env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: JNI Env is %p\n", jni_env);
    fprintf(stderr, "\tnative: current thread %p\n", thread);
    fprintf(stderr, "\tnative: current method %p\n", method);
    fprintf(stderr, "\tnative: current location %lld\n", location);
    fprintf(stderr, "\tnative: current class %p \n", field_klass);
    fprintf(stderr, "\tnative: current object %p\n", object);
    fprintf(stderr, "\tnative: current field %p\n", field);
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}
/***************************************************************************/

