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
 * @version $Revision: 1.1.1.1 $
 *
 */

#include "utils.h"
#include "events.h"

/* *********************************************************************** */

#if 1

void SingleStep
(jvmtiEnv*, JNIEnv*, jthread, jmethodID, jlocation)
{ return; }

void Breakpoint
(jvmtiEnv*, JNIEnv*, jthread, jmethodID, jlocation)
{ return; }

void FieldAccess
(jvmtiEnv*, JNIEnv*, jthread, jmethodID, jlocation, jclass, jobject, jfieldID)
{ return; }
void FieldModification
(jvmtiEnv*, JNIEnv*, jthread, jmethodID, jlocation, jclass, jobject, jfieldID, char, jvalue)
{ return; }

void FramePop
(jvmtiEnv*, JNIEnv*, jthread, jmethodID, jboolean)
{ return; }

void MethodEntry
(jvmtiEnv*, JNIEnv*, jthread, jmethodID)
{ return; }

void MethodExit
(jvmtiEnv*, JNIEnv*, jthread, jmethodID, jboolean, jvalue)
{ return; }

void NativeMethodBind
(jvmtiEnv*, JNIEnv*, jthread, jmethodID, void*, void**)
{ return; }

void Exception
(jvmtiEnv*, JNIEnv*, jthread, jmethodID, jlocation, jobject, jmethodID, jlocation)
{ return; }

void ExceptionCatch
(jvmtiEnv*, JNIEnv*, jthread, jmethodID, jlocation, jobject)
{ return; }

void ThreadStart
(jvmtiEnv*, JNIEnv*, jthread)
{ return; }

void ThreadEnd
(jvmtiEnv*, JNIEnv*, jthread)
{ return; }

void ClassLoad
(jvmtiEnv*, JNIEnv*, jthread, jclass)
{ return; }

void ClassPrepare
(jvmtiEnv*, JNIEnv*, jthread, jclass)
{ return; }

void ClassFileLoadHook
(jvmtiEnv*, JNIEnv*, jclass, jobject, const char*, jobject, jint,
 const unsigned char*, jint*, unsigned char**)
{ return; }

void VMStart
(jvmtiEnv*, JNIEnv*)
{ return; }

void VMInit
(jvmtiEnv*, JNIEnv*, jthread)
{ return; }

void VMDeath
(jvmtiEnv*, JNIEnv*)
{ return; }

void CompiledMethodLoad
(jvmtiEnv*, jmethodID, jint, const void*, jint,
 const jvmtiAddrLocationMap*, const void*)
{ return; }

void CompiledMethodUnload
(jvmtiEnv *, jmethodID, const void*)
{ return; }

void DynamicCodeGenerated
(jvmtiEnv*, const char*, const void*, jint)
{ return; }

void DataDumpRequest
(jvmtiEnv*)
{ return; }

void MonitorContendedEnter
(jvmtiEnv*, JNIEnv*, jthread, jobject)
{ return; }

void MonitorContendedEntered
(jvmtiEnv*, JNIEnv*, jthread, jobject)
{ return; }

void MonitorWait
(jvmtiEnv*, JNIEnv*, jthread, jobject, jlong)
{ return; }

void MonitorWaited
(jvmtiEnv*, JNIEnv*, jthread, jobject, jboolean)
{ return; }

void VMObjectAlloc
(jvmtiEnv*, JNIEnv*, jthread, jobject, jclass, jlong)
{ return; }

void ObjectFree
(jvmtiEnv*, jlong)
{ return; }

void GarbageCollectionStart
(jvmtiEnv*)
{ return; }

void GarbageCollectionFinish
(jvmtiEnv*)
{ return; }

#endif

/* *********************************************************************** */

Callbacks::Callbacks()
{
    cbSingleStep = (jvmtiEventSingleStep)SingleStep;
    cbBreakpoint = (jvmtiEventBreakpoint)Breakpoint;
    cbFieldAccess = (jvmtiEventFieldAccess)FieldAccess;
    cbFieldModification = (jvmtiEventFieldModification)FieldModification;
    cbFramePop = (jvmtiEventFramePop)FramePop;
    cbMethodEntry = (jvmtiEventMethodEntry)MethodEntry;
    cbMethodExit = (jvmtiEventMethodExit)MethodExit;
    cbNativeMethodBind = (jvmtiEventNativeMethodBind)NativeMethodBind;
    cbException = (jvmtiEventException)Exception;
    cbExceptionCatch = (jvmtiEventExceptionCatch)ExceptionCatch;
    cbThreadStart = (jvmtiEventThreadStart)ThreadStart;
    cbThreadEnd = (jvmtiEventThreadEnd)ThreadEnd;
    cbClassLoad = (jvmtiEventClassLoad)ClassLoad;
    cbClassPrepare = (jvmtiEventClassPrepare)ClassPrepare;
    cbClassFileLoadHook = (jvmtiEventClassFileLoadHook)ClassFileLoadHook;
    cbVMStart = (jvmtiEventVMStart)VMStart;
    cbVMInit = (jvmtiEventVMInit)VMInit;
    cbVMDeath = (jvmtiEventVMDeath)VMDeath;
    cbCompiledMethodLoad = (jvmtiEventCompiledMethodLoad)CompiledMethodLoad;
    cbCompiledMethodUnload = (jvmtiEventCompiledMethodUnload)CompiledMethodUnload;
    cbDynamicCodeGenerated = (jvmtiEventDynamicCodeGenerated)DynamicCodeGenerated;
    cbDataDumpRequest = (jvmtiEventDataDumpRequest)DataDumpRequest;
    cbMonitorContendedEnter = (jvmtiEventMonitorContendedEnter)MonitorContendedEnter;
    cbMonitorContendedEntered = (jvmtiEventMonitorContendedEntered)MonitorContendedEntered;
    cbMonitorWait = (jvmtiEventMonitorWait)MonitorWait;
    cbMonitorWaited = (jvmtiEventMonitorWaited)MonitorWaited;
    cbVMObjectAlloc = (jvmtiEventVMObjectAlloc)VMObjectAlloc;
    cbObjectFree = (jvmtiEventObjectFree)ObjectFree;
    cbGarbageCollectionStart = (jvmtiEventGarbageCollectionStart)GarbageCollectionStart;
    cbGarbageCollectionFinish = (jvmtiEventGarbageCollectionFinish)GarbageCollectionFinish;
}

/* *********************************************************************** */

