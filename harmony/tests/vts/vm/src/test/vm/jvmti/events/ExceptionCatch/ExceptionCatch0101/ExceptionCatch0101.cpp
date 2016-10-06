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
static bool test = false;
static long counter = 0;

/* *********************************************************************** */

/**
 * test of event ExceptionCatch
 */
void ExceptionCatch0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_exccatch;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_EXCEPTION_CATCH, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest ExceptionCatch0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackExceptionCatch(prms_EXCPT_CATCH)
{
    check_EXCPT_CATCH;

    char* name = NULL;
    char* signature = NULL;
    char* generic = NULL;
    jvmtiError result;

    result = jvmti_env->GetMethodName(method, &name, &signature, &generic);
    if (result != JVMTI_ERROR_NONE) return;
    counter++;
    if (strcmp(name, "main")) return;

    fprintf(stderr, "\tnative: Exception (prms) jvmti_env      is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: Exception (prms) jni_env        is %p\n", jni_env);
    fprintf(stderr, "\tnative: Exception (prms) thread         is %p\n", thread);
    fprintf(stderr, "\tnative: Exception (prms) method         is %p\n", method);
    fprintf(stderr, "\tnative: Exception (prms) location       is %lld\n", location);
    fprintf(stderr, "\tnative: Exception (prms) exception      is %p\n", exception);
    fflush(stderr);

    counter++;

    //this check must be fixed
    params = true;

    test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event ExceptionCatch0101                 : ");

    if ((counter > 0) && (params) && (test))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test ExceptionCatch0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */







