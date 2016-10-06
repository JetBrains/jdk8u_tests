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
 * test of event VMObjectAlloc0101
 */
void VMObjectAlloc0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_obj;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_VM_OBJECT_ALLOC, JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest VMObjectAlloc0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackVMObjectAlloc(prms_VMOBJ_ALLOC)
{
    char* signature;
    char* generic;
    jvmtiError result;

    counter++;

    fprintf(stderr, "\tnative: VMObjectAlloc (prms) jvmti_env is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: VMObjectAlloc (prms) jni_env is %p\n", jni_env);
    fprintf(stderr, "\tnative: VMObjectAlloc (prms) thread is %p\n", thread);
    fprintf(stderr, "\tnative: VMObjectAlloc (prms) object is %p\n", object);
    fprintf(stderr, "\tnative: VMObjectAlloc (prms) object_klass is %p\n", object_klass);

    {
        result = jvmti_env->GetClassSignature(object_klass, &signature,&generic);
        fprintf(stderr, "\t\tnative: GetClassSignature result = %d (must be zero) \n", result);
        fprintf(stderr, "\t\tnative: object_klass ptr is %p \n", object_klass);
        fprintf(stderr, "\t\tnative: signature is %s \n", signature);
        //fprintf(stderr, "\t\tnative: generic is %s \n", generic);
    }

    fprintf(stderr, "\tnative: VMObjectAlloc (prms) size is %lld\n", (long long)size);
    fprintf(stderr, "\tnative: VMObjectAlloc counter is %lld\n", (long long)counter);
    fflush(stderr);

    if (jvmti_env && jni_env)
        params = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event VMObjectAlloc0101              : ");

    if ((counter >= 1) && (params))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test VMObjectAlloc0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */








