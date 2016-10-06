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

static bool params = false;
static long counter = 0;

/* *********************************************************************** */

/**
 * test of event MethodExit
 */
void MethodExit0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_mexit;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_METHOD_EXIT, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest MethodExit0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackMethodExit(prms_METHOD_EXIT)
{
//    check_METHOD_EXIT;

    char* name = NULL;
    char* signature = NULL;
    char* generic = NULL;
    jvmtiError result;

    result = jvmti_env->GetMethodName(method, &name, &signature, &generic);

    if (result != JVMTI_ERROR_NONE) return;

    counter++;

    if (strcmp(name, "special_method")) return;

    fprintf(stderr, "\tnative: MethodExit (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: MethodExit (prms) jni_env   is %p\n", jni_env);
    fprintf(stderr, "\tnative: MethodExit (prms) thread    is %p\n", thread);
    fprintf(stderr, "\tnative: MethodExit (prms) method    is %p\n", method);
    fflush(stderr);

    fprintf(stderr, "\tnative: GetMethodName result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: method              is %p \n", method);
    fprintf(stderr, "\tnative: name                is %s \n", name);
    fprintf(stderr, "\tnative: signature           is %s \n", signature);
    fprintf(stderr, "\tnative: generic             is %s \n", generic);
    fflush(stderr);

    params = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event MethodExit0101                     : ");

    if (params)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test MethodExit0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */



