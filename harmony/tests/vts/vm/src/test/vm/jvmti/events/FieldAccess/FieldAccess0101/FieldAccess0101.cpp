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

static bool finish = false;
static bool params = false;
static bool test = true;
static long counter = 0;

/* *********************************************************************** */

/**
 * test of event FieldAccess
 */
void FieldAccess0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

JNIEXPORT jint JNICALL Agent_OnLoad(prms_AGENT_ONLOAD)
{
    check_AGENT_ONLOAD;

    Callbacks CB;

    cb_exc;
    cb_faccess;
    cb_death;

    AGENT_FOR_EVENTS_TESTS_PART_I; /* events.h */

    jvmtiEvent events[] = { JVMTI_EVENT_EXCEPTION, JVMTI_EVENT_FIELD_ACCESS,
        JVMTI_EVENT_VM_DEATH };

    AGENT_FOR_EVENTS_TESTS_PART_II;

    fprintf(stderr, "\n-------------------------------------------------\n");
    fprintf(stderr, "\ntest FieldAccess0101 is started\n{\n");
    fflush(stderr);

    return JNI_OK;
}

/* *********************************************************************** */

void JNICALL callbackException(prms_EXCPT)
{
    check_EXCPT;

    char* name;
    char* signature;
    char* generic;

    jint class_count;
    jclass* classes;
    char* source_name_ptr;
    char* name_ptr;
    char* signature_ptr;
    char* generic_ptr;
    jint field_count_ptr;
    jfieldID* fields_ptr;
    jvmtiError result;

    result = jvmti_env->GetMethodName(method, &name, &signature, &generic);
    if (result != JVMTI_ERROR_NONE) return;
    if (strcmp(name, "special_method")) return;
    result = jvmti_env->GetLoadedClasses(&class_count, &classes);
    if ( result != JVMTI_ERROR_NONE) return;

    for (int i = 0; i < class_count; i++)
    {
        if (!test || finish) break;
        result = jvmti_env->GetSourceFileName(classes[i], &source_name_ptr);
        if (result != JVMTI_ERROR_NONE) continue;
        if (strcmp(source_name_ptr, "FieldAccess0101.java")) continue;
        result = jvmti_env->GetClassFields(classes[i], &field_count_ptr, &fields_ptr);
        if (result != JVMTI_ERROR_NONE) continue;
        for (int j = 0; j < field_count_ptr; j++)
        {
            if (!test || finish) break;

            result = jvmti_env->GetFieldName(classes[i], fields_ptr[j], &name_ptr,
                    &signature_ptr, &generic_ptr);

            if (result != JVMTI_ERROR_NONE) continue;
            if (strcmp(name_ptr, "first_field")) continue;

            result = jvmti_env->SetFieldAccessWatch(classes[i], fields_ptr[j]);

            fprintf(stderr, "\tnative: SetFieldAccessWatch result is %d\n", result);
            fprintf(stderr, "\tnative: class  is %p\n", classes[i]);
            fprintf(stderr, "\tnative: field  is %p\n\n", fields_ptr[j]);
            fflush(stderr);

            if (result != JVMTI_ERROR_NONE)
                test = false;
            else
                finish = true;
        }
    }
}

void JNICALL callbackFieldAccess(prms_FLD_ACCESS)
{
    check_FLD_ACCESS;

    fprintf(stderr, "\tnative: FieldAccess (prms) jvmti_env   is %p\n", jvmti_env);
    fprintf(stderr, "\tnative: FieldAccess (prms) jni_env     is %p\n", jni_env);
    fprintf(stderr, "\tnative: FieldAccess (prms) thread      is %p\n", thread);
    fprintf(stderr, "\tnative: FieldAccess (prms) method      is %p\n", method);
    fprintf(stderr, "\tnative: FieldAccess (prms) location    is %lld\n", location);
    fprintf(stderr, "\tnative: FieldAccess (prms) field_klass is %p\n", field_klass);
    fprintf(stderr, "\tnative: FieldAccess (prms) object      is %p\n", object);
    fprintf(stderr, "\tnative: FieldAccess (prms) field       is %p\n", field);
    fflush(stderr);

    counter++;

    //this check must be fixed
    params = true;
}

void JNICALL callbackVMDeath(prms_VMDEATH)
{
    check_VMDEATH;

    fprintf(stderr, "\n\tTest of event FieldAccess0101                    : ");

    if ((counter == 1) && (params) && (test))
        fprintf(stderr, " passed \n");
    else
        fprintf(stderr, " failed \n");

    fprintf(stderr, "\n} /* test FieldAccess0101 is finished */ \n");
    fflush(stderr);
}

/* *********************************************************************** */


