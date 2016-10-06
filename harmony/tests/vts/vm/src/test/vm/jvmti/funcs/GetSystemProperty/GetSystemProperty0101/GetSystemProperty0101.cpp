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
 * @version $Revision: 1.3 $
 *
 */

/* *********************************************************************** */

#include "events.h"
#include "utils.h"
#include "fake.h"

static bool test = false;
static bool util = false;

const char test_case_name[] = "GetSystemProperty0101";

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = { JVMTI_EVENT_VM_INIT, JVMTI_EVENT_VM_DEATH };
    cb_init;
    cb_death;
    return func_for_Agent_OnLoad(vm, options, reserved, &CB, events,
            sizeof(events)/4, test_case_name, DEBUG_OUT);
}

/* *********************************************************************** */

void JNICALL callbackVMInit(prms_VMINIT)
{
    check_VMINIT;
    char* value;
    jvmtiError result;

    result = jvmti_env->GetSystemProperty("java.bla_bla_bla.property", &value);
    util = true;
    fprintf(stderr, "\tnative: GetSystemProperty result = %d (must be zero) \n", result);
    fprintf(stderr, "\tnative: property name     is java.bla_bla_bla.property \n");
    if (result != JVMTI_ERROR_NONE) return;
    fprintf(stderr, "\tnative: property value    is '%s' \n", value);
    fprintf(stderr, "\tnative: strcmp('%s', 'bla_bla_bla_bla_bla') result is %d \n", value, strcmp(value, "bla_bla_bla_bla_bla"));
    
//    if (strcmp(value, "bla_bla_bla_bla_bla")) test = true;
	test = (strncmp(value, "bla_bla_bla_bla_bla", strlen("bla_bla_bla_bla_bla"))==0);
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, util);
}

/* *********************************************************************** */

