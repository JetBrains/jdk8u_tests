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
static bool flag = false;
static long counter = 0;

/* *********************************************************************** */

/**
 * test of event ClassFileLoadHook
 */
void ClassFileLoadHook0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_loadhook;
    cb_tstart;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, JVMTI_EVENT_VM_DEATH,
        JVMTI_EVENT_THREAD_START };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest ClassFileLoadHook0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackClassFileLoadHook(prms_CLS_FL_LD_HOOK)
{
    check_CLS_FL_LD_HOOK;

    if (flag) return;

    jvmtiError result;

    fprintf(stderr, "\tnative: ClassFileLoadHook (prms) name                  is %s\n\n", name);

    if (strcmp(name, "org/apache/harmony/vts/test/vm/jvmti/ClassFileLoadHook0101"))
        return;

    fprintf(stderr, "\tnative: ClassFileLoadHook (prms) jvmti_env             is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: ClassFileLoadHook (prms) jni_env               is %p\n", jni_env);
    fprintf(stderr, "\tnative: ClassFileLoadHook (prms) class_being_redefined is %p\n", class_being_redefined);
    fprintf(stderr, "\tnative: ClassFileLoadHook (prms) loader                is %p\n", loader);
    fprintf(stderr, "\tnative: ClassFileLoadHook (prms) name                  is %s\n", name);
    fprintf(stderr, "\tnative: ClassFileLoadHook (prms) protection_domain     is %p\n", protection_domain);
    fprintf(stderr, "\tnative: ClassFileLoadHook (prms) class_data_len        is %d\n", class_data_len);
    fprintf(stderr, "\tnative: ClassFileLoadHook (prms) class_data            is %p\n", class_data);
    fprintf(stderr, "\tnative: ClassFileLoadHook (prms) new_class_data_len    is %d\n", *new_class_data_len);
    fprintf(stderr, "\tnative: ClassFileLoadHook (prms) new_class_data        is %p\n", new_class_data);
    fflush(stderr);

    counter++;

    params = true;
    flag = true;

    *new_class_data_len = class_data_len;

    result = jvmti_env->Allocate(class_data_len, new_class_data);

    if ( result != JVMTI_ERROR_NONE) return;

    *new_class_data_len = class_data_len;

    memcpy((*new_class_data), class_data, class_data_len);

    for (int i = 0; i < (class_data_len - 3); i++)
    {
        if ((class_data[  i  ] == 0xde) &&
                (class_data[i + 1] == 0xad) &&
                (class_data[i + 2] == 0xbe) &&
                (class_data[i + 3] == 0xef))
        {
            (*new_class_data)[  i  ] = 0xba;
            (*new_class_data)[i + 1] = 0xbe;
            (*new_class_data)[i + 2] = 0xf0;
            (*new_class_data)[i + 3] = 0x0d;

            fprintf(stderr, "\tnative: class data was changed");
            fflush(stderr);
        }
    }
}

void JNICALL callbackThreadStart(prms_THRD_START)
{
    check_THRD_START;

    jvmtiPhase phase;
    bool thread_event_second_passed = false;
    bool thread_event_second_failed = false;
    jvmtiThreadInfo tinfo;

    if (jvmti_env->GetPhase(&phase) != JVMTI_ERROR_NONE)

    if (phase != JVMTI_PHASE_LIVE) return;

    if (jvmti_env->GetThreadInfo(thread, &tinfo) != JVMTI_ERROR_NONE)
        return;

    if (!strcmp(tinfo.name, "agent_second_passed"))
        thread_event_second_passed = true;

    if (!strcmp(tinfo.name, "agent_second_failed"))
        thread_event_second_failed = true;

    if (thread_event_second_passed || thread_event_second_failed)
    {
        counter++;

        if (thread_event_second_passed)
            test = true;
    }
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event ClassFileLoadHook              : ");

    if ((counter == 2) && (params) && (test))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test ClassFileLoadHook0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */


