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

/* *********************************************************************** */

/**
 * test of function Deallocate
 */
void Deallocate0103()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_init;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_VM_INIT, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest Deallocate0103 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackVMInit(prms_VMINIT)
{
    check_VMINIT;

    jlong size = -1;
    unsigned char* mem_ptr = NULL;
    jvmtiError result_util;
    jvmtiError result;

    result_util = jvmti_env->Allocate(size, &mem_ptr);
    fprintf(stderr, "\n\tnative: Allocate for size < 0\n");
    fprintf(stderr, "\tnative: Allocate result = %d (must be 103) \n", result_util);
    fflush(stderr);

    if (result_util != JVMTI_ERROR_ILLEGAL_ARGUMENT) return;

    util = true;

    result = jvmti_env->Deallocate(mem_ptr);
    fprintf(stderr, "\n\tnative: Deallocate for mem_ptr_3 = NULL after error\n");
    fprintf(stderr, "\tnative: Deallocate result = %d (must be 0) \n", result);
    fflush(stderr);

    if (result != JVMTI_ERROR_NONE) return;

    test = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of function Deallocate0103                  : ");

    if (test && util)
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test Deallocate0103 is finished */ \n\n");
    fflush(stderr);
}

/* *********************************************************************** */


