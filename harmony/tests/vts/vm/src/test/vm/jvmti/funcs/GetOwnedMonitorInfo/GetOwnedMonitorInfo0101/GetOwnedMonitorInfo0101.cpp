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

/* *********************************************************************** */

/**
 * test of function GetOwnedMonitorInfo
 */
void GetOwnedMonitorInfo0101()
{
    //Fake method for docletting only
}

/* *********************************************************************** */

#include <iostream>
#include <cstring>
#include <jvmti.h>

using namespace std;

static const char* EXCEPTION_CLASS = "InvokeAgentException;";

bool is_passed = false;

#define TURN_EVENT(event, state) { \
    jvmtiError err = turn_event(jvmti, event, state, #event); \
    if (JVMTI_ERROR_NONE != err) return; \
}

#define CHECK_RESULT(func) \
    if (JVMTI_ERROR_NONE != err) { \
        cerr << "[JvmtiAgent] ERROR: " << #func << " failed with error: " << err << endl;  \
        return; \
    }

#define CHECK_JNI3(result, func, error_code) { \
    if (jni->ExceptionCheck()) { \
        cerr << "[JvmtiAgent] ERROR: unexpected exception in " << #func << endl;  \
        jni->ExceptionDescribe(); \
        return error_code; \
    } \
    if (! (result)) { \
        cerr << "[JvmtiAgent] ERROR: get NULL in " << #func << endl;  \
        return error_code; \
    } \
}

#define CHECK_JNI(result, func) CHECK_JNI3(result, func, )

static jvmtiError turn_event(jvmtiEnv* jvmti, jvmtiEvent event, bool state,
        const char* event_name)
{
    jvmtiError err;
    err = jvmti->SetEventNotificationMode(state ? JVMTI_ENABLE : JVMTI_DISABLE,
            event, NULL);
    if (JVMTI_ERROR_NONE != err) {
        cerr << "[JvmtiAgent] ERROR: unable to " << (state ? "en" : "dis")
                << "able " << event_name
                << endl;
    }

    return err;
}

static void JNICALL VMInit(jvmtiEnv* jvmti, JNIEnv* jni, jthread thread)
{
    cerr << endl << "[JvmtiAgent] ==> VM Init callback" << endl;

    TURN_EVENT(JVMTI_EVENT_EXCEPTION, true);
    TURN_EVENT(JVMTI_EVENT_VM_DEATH, true);
}

static void JNICALL VMDeath(jvmtiEnv* jvmti, JNIEnv* jni)
{
    cerr << endl << "[JvmtiAgent] ==> VM Death callback" << endl;

    cerr << endl << "\tTest of function GetOwnedMonitorInfo0101         :  "
            << (is_passed ? "passed" : "failed") << " " << endl;
}

static void JNICALL
Exception(jvmtiEnv *jvmti,
            JNIEnv* jni,
            jthread thread,
            jmethodID method,
            jlocation location,
            jobject exception,
            jmethodID catch_method,
            jlocation catch_location)
{
    jvmtiError err;

    jclass exn_class = jni->GetObjectClass(exception);
    CHECK_JNI(exn_class, GetObjectClass);

    char* class_name = NULL;
    err = jvmti->GetClassSignature(exn_class, &class_name, NULL);
    CHECK_RESULT(GetClassSignature);

    if (NULL == strstr(class_name, EXCEPTION_CLASS))
        return;

    cerr << "[JvmtiAgent] ==> Exception callback" << endl;
    cerr << "[JvmtiAgent]     for class: " << class_name << endl;

    jfieldID field;

    field = jni->GetFieldID(exn_class, "ownerThread", "Ljava/lang/Thread;");
    CHECK_JNI(field, GetFieldID);

    jthread owner_thread = jni->GetObjectField(exception, field);
    CHECK_JNI(owner_thread, GetObjectField);

    field = jni->GetFieldID(exn_class, "monitors", "[Ljava/lang/Object;");
    CHECK_JNI(field, GetFieldID);

    jobjectArray monitors = (jobjectArray) jni->GetObjectField(exception,
            field);
    CHECK_JNI(monitors, GetObjectField);

    jint expected_monitor_count = (jint) jni->GetArrayLength(monitors);
    CHECK_JNI(expected_monitor_count, GetArrayLength);

    jint owned_monitor_count;
    jobject* owned_monitors;

    cerr << "[JvmtiAgent]     Getting monitor info..." << endl;
    err = jvmti->GetOwnedMonitorInfo(owner_thread, &owned_monitor_count,
            &owned_monitors);
    CHECK_RESULT(GetOwnedMonitorInfo);

    cerr << "[JvmtiAgent]     Owned monitor count    "
            << owned_monitor_count << endl;
    cerr << "[JvmtiAgent]     Expected monitor count "
            << expected_monitor_count << endl;

    if (owned_monitor_count != expected_monitor_count) {
        cerr << "[JvmtiAgent]     ERROR: Wrong monitor count." << endl;

        err = jvmti->Deallocate((unsigned char*) owned_monitors);
        CHECK_RESULT(Deallocate);
        return;
    }

    for (jint i = 0; i < owned_monitor_count; i++) {
        cerr << "[JvmtiAgent]     Check monitor number " << i << endl;

        jobject expected_monitor = jni->GetObjectArrayElement(monitors,
                owned_monitor_count - 1 - i);
        CHECK_JNI(expected_monitor, GetObjectArrayElement);

        if (! jni->IsSameObject(expected_monitor, owned_monitors[i])) {
            cerr << "[JvmtiAgent]     ERROR: Owned monitor number " << i
                     << " doesn't match the expected one." << endl;

            err = jvmti->Deallocate((unsigned char*) owned_monitors);
            CHECK_RESULT(Deallocate);
            return;
        }
    }

    err = jvmti->Deallocate((unsigned char*) owned_monitors);
    CHECK_RESULT(Deallocate);

    is_passed = true;
}

JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *vm, char *options, void *reserved)
{
    jvmtiEnv *jvmti = NULL;
    jvmtiError err;

    // Get JVMTI interface pointer
    jint iRes = vm->GetEnv((void**)&jvmti, JVMTI_VERSION);
    if (JNI_OK != iRes) {
        cerr << "[JvmtiAgent] ERROR: unable to get JVMTI environment" << endl;
        return -1;
    }

    // Set events callbacks
    jvmtiEventCallbacks callbacks;
    memset(&callbacks, 0, sizeof(jvmtiEventCallbacks));

    callbacks.VMInit = VMInit;
    callbacks.VMDeath = VMDeath;
    callbacks.Exception = Exception;

    err = jvmti->SetEventCallbacks(&callbacks, sizeof(jvmtiEventCallbacks));
    if (JVMTI_ERROR_NONE != err) {
        cerr << "[JvmtiAgent] ERROR: unable to register event callbacks" << endl;
        return -1;
    }

    err = jvmti->SetEventNotificationMode(JVMTI_ENABLE,
            JVMTI_EVENT_VM_INIT, NULL);
    if (JVMTI_ERROR_NONE != err) {
        cerr << "[JvmtiAgent] ERROR: unable to enable VMInit event"
                << endl;
        return -1;
    }

    // Set capabilities
    jvmtiCapabilities capabilities;
    memset(&capabilities, 0, sizeof(jvmtiCapabilities));
    capabilities.can_generate_exception_events = 1;
    capabilities.can_get_owned_monitor_info = 1;

    err = jvmti->AddCapabilities(&capabilities);
    if (JVMTI_ERROR_NONE != err) {
        cerr << "[JvmtiAgent] ERROR: unable to possess capabilities" << endl;
        return -1;
    }

    // Agent initialized successfully
    return 0;
}
