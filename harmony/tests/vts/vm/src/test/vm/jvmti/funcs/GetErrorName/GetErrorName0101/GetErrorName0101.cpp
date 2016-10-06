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

const char test_case_name[] = "GetErrorName0101";

/* *********************************************************************** */

static char* errorNames[117] = {

    "JVMTI_ERROR_NONE", // = 0,
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
    "JVMTI_ERROR_INVALID_THREAD",         // = 10,
    "JVMTI_ERROR_INVALID_THREAD_GROUP",   // = 11,
    "JVMTI_ERROR_INVALID_PRIORITY",       // = 12,
    "JVMTI_ERROR_THREAD_NOT_SUSPENDED",   // = 13,
    "JVMTI_ERROR_THREAD_SUSPENDED",       // = 14,
    "JVMTI_ERROR_THREAD_NOT_ALIVE",       // = 15,
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
    "JVMTI_ERROR_INVALID_OBJECT",         // = 20,
    "JVMTI_ERROR_INVALID_CLASS",          // = 21,
    "JVMTI_ERROR_CLASS_NOT_PREPARED",     // = 22,
    "JVMTI_ERROR_INVALID_METHODID",       // = 23,
    "JVMTI_ERROR_INVALID_LOCATION",       // = 24,
    "JVMTI_ERROR_INVALID_FIELDID",        // = 25,
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
    "JVMTI_ERROR_NO_MORE_FRAMES",         // = 31,
    "JVMTI_ERROR_OPAQUE_FRAME",           // = 32,
        "Illegal Error Number",
    "JVMTI_ERROR_TYPE_MISMATCH",          // = 34,
    "JVMTI_ERROR_INVALID_SLOT",           // = 35,
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
    "JVMTI_ERROR_DUPLICATE",              // = 40,
    "JVMTI_ERROR_NOT_FOUND",              // = 41,
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
    "JVMTI_ERROR_INVALID_MONITOR",   // = 50,
    "JVMTI_ERROR_NOT_MONITOR_OWNER", // = 51,
    "JVMTI_ERROR_INTERRUPT",         // = 52,
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
    "JVMTI_ERROR_INVALID_CLASS_FORMAT",                              // = 60,
    "JVMTI_ERROR_CIRCULAR_CLASS_DEFINITION",                         // = 61,
    "JVMTI_ERROR_FAILS_VERIFICATION",                                // = 62,
    "JVMTI_ERROR_UNSUPPORTED_REDEFINITION_METHOD_ADDED",             // = 63,
    "JVMTI_ERROR_UNSUPPORTED_REDEFINITION_SCHEMA_CHANGED",           // = 64,
    "JVMTI_ERROR_INVALID_TYPESTATE",                                 // = 65,
    "JVMTI_ERROR_UNSUPPORTED_REDEFINITION_HIERARCHY_CHANGED",        // = 66,
    "JVMTI_ERROR_UNSUPPORTED_REDEFINITION_METHOD_DELETED",           // = 67,
    "JVMTI_ERROR_UNSUPPORTED_VERSION",                               // = 68,
    "JVMTI_ERROR_NAMES_DONT_MATCH",                                  // = 69,
    "JVMTI_ERROR_UNSUPPORTED_REDEFINITION_CLASS_MODIFIERS_CHANGED",  // = 70,
    "JVMTI_ERROR_UNSUPPORTED_REDEFINITION_METHOD_MODIFIERS_CHANGED", // = 71,
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
    "JVMTI_ERROR_UNMODIFIABLE_CLASS", // = 79,
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
    "JVMTI_ERROR_NOT_AVAILABLE",           // = 98,
    "JVMTI_ERROR_MUST_POSSESS_CAPABILITY", // = 99,
    "JVMTI_ERROR_NULL_POINTER",            // = 100,
    "JVMTI_ERROR_ABSENT_INFORMATION",      // = 101,
    "JVMTI_ERROR_INVALID_EVENT_TYPE",      // = 102,
    "JVMTI_ERROR_ILLEGAL_ARGUMENT",        // = 103,
    "JVMTI_ERROR_NATIVE_METHOD",           // = 104,
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
        "Illegal Error Number",
    "JVMTI_ERROR_OUT_OF_MEMORY",       // = 110,
    "JVMTI_ERROR_ACCESS_DENIED",       // = 111,
    "JVMTI_ERROR_WRONG_PHASE",         // = 112,
    "JVMTI_ERROR_INTERNAL",            // = 113,
        "Illegal Error Number",
    "JVMTI_ERROR_UNATTACHED_THREAD",   // = 115,
    "JVMTI_ERROR_INVALID_ENVIRONMENT"  // = 116
};

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    Callbacks CB;
    check_AGENT_ONLOAD;
    jvmtiEvent events[] = {JVMTI_EVENT_VM_DEATH };
    cb_death;
    jvmtiEnv *jvmti;
    jvmtiError result;
    jint res = func_for_Agent_OnLoad_JVMTI(vm, options, reserved, &CB,
        events, sizeof(events)/4, test_case_name, DEBUG_OUT, &jvmti);

    jint count_test = 0;
    char* name;

    for (int i = 0; i <= 116; i++ )
    {
        jvmtiError result = jvmti->GetErrorName((jvmtiError)i, &name);
        fprintf(stderr, "\tnative: GetErrorName result = %d (must be zero or 103) \n", result);
        fprintf(stderr, "\tnative: error number i = %d has name %s \n", i ,name);
        fflush(stderr);

        if (result == JVMTI_ERROR_NONE)
        {
            if (strcmp(name, errorNames[(int)i]) == 0)
                count_test++;
            else
                break;
        }
        else
        {
            if (result == JVMTI_ERROR_ILLEGAL_ARGUMENT)
            {
                if (!strcmp(errorNames[(int)i],"Illegal Error Number"))
                    count_test++;
                else
                    break;
            }
            jvmti->Deallocate((unsigned char*)name);
        }
        name = NULL;
    }

    if ( count_test == 117 ) test = true;
    return JNI_OK;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;
    func_for_callback_VMDeath(jni_env, jvmti_env, test_case_name, test, true);
}

/* *********************************************************************** */

