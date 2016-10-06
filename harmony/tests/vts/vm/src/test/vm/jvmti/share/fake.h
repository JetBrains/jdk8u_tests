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

#ifndef _FAKE_H_
#define _FAKE_H_

void Allocate0101();
void Allocate0102();
void Allocate0103();

void Deallocate0101();
void Deallocate0102();
void Deallocate0103();

void GetThreadState0101();
void GetThreadState0102();
void GetThreadState0103();

void GetAllThreads0101();
void GetAllThreads0102();
void GetAllThreads0103();

void SuspendThread0101();
void SuspendThread0102();
void SuspendThread0103();
void SuspendThread0104();
void SuspendThread0105();

void SuspendThreadList0101();
void SuspendThreadList0102();
void SuspendThreadList0103();
void SuspendThreadList0104();
void SuspendThreadList0105();

void ResumeThread0101();
void ResumeThread0102();
void ResumeThread0103();
void ResumeThread0104();
void ResumeThread0105();
void ResumeThread0106();

void ResumeThreadList0101();
void ResumeThreadList0102();
void ResumeThreadList0103();
void ResumeThreadList0104();
void ResumeThreadList0105();

void StopThread0101();
void StopThread0102();
void StopThread0103();
void StopThread0104();
void StopThread0105();

void InterruptThread0101();
void InterruptThread0102();
void InterruptThread0103();
void InterruptThread0104();

void GetThreadInfo0101();
void GetThreadInfo0102();
void GetThreadInfo0103();

void GetOwnedMonitorInfo0101();
void GetOwnedMonitorInfo0102();
void GetOwnedMonitorInfo0103();
void GetOwnedMonitorInfo0104();
void GetOwnedMonitorInfo0105();
void GetOwnedMonitorInfo0106();

void GetCurrentContendedMonitor0101();
void GetCurrentContendedMonitor0102();
void GetCurrentContendedMonitor0103();
void GetCurrentContendedMonitor0104();
void GetCurrentContendedMonitor0105();

void RunAgentThread0101();
void RunAgentThread0102();
void RunAgentThread0103();
void RunAgentThread0104();

void SetThreadLocalStorage0101();
void SetThreadLocalStorage0102();
void SetThreadLocalStorage0103();

void GetThreadLocalStorage0101();
void GetThreadLocalStorage0102();
void GetThreadLocalStorage0103();

void GetTopThreadGroups0101();
void GetTopThreadGroups0102();
void GetTopThreadGroups0103();

void GetThreadGroupInfo0101();
void GetThreadGroupInfo0102();
void GetThreadGroupInfo0103();

void GetThreadGroupChildren0101();
void GetThreadGroupChildren0102();
void GetThreadGroupChildren0103();
void GetThreadGroupChildren0104();
void GetThreadGroupChildren0105();
void GetThreadGroupChildren0106();

void GetStackTrace0101();
void GetStackTrace0102();
void GetStackTrace0103();
void GetStackTrace0104();
void GetStackTrace0105();
void GetStackTrace0106();
void GetStackTrace0107();

void GetAllStackTraces0101();
void GetAllStackTraces0102();
void GetAllStackTraces0103();
void GetAllStackTraces0104();

void GetThreadListStackTraces0101();
void GetThreadListStackTraces0102();
void GetThreadListStackTraces0103();
void GetThreadListStackTraces0104();
void GetThreadListStackTraces0105();

void GetFrameCount0101();
void GetFrameCount0102();
void GetFrameCount0103();
void GetFrameCount010();

void PopFrame0101();
void PopFrame0102();
void PopFrame0104();
void PopFrame0105();
void PopFrame0106();
void PopFrame0107();

void GetFrameLocation0101();
void GetFrameLocation0102();
void GetFrameLocation0103();
void GetFrameLocation0104();
void GetFrameLocation0105();
void GetFrameLocation0106();
void GetFrameLocation0107();

void NotifyFramePop0101();
void NotifyFramePop0102();
void NotifyFramePop0103();
void NotifyFramePop0104();
void NotifyFramePop0105();
void NotifyFramePop0106();
void NotifyFramePop0107();

void GetTag0101();
void GetTag0102();
void GetTag0103();
void GetTag0104();

void SetTag0101();
void SetTag0102();
void SetTag0103();

void ForceGarbageCollection0101();

void IterateObjectsFromObject0101();
void IterateObjectsFromObject0102();
void IterateObjectsFromObject0103();
void IterateObjectsFromObject0104();

void IterateObjects0101();
void IterateObjects0102();

void IterateOverHeap0101();
void IterateOverHeap0102();
void IterateOverHeap0103();

void IterateInstances0101();
void IterateInstances0102();
void IterateInstances0103();
void IterateInstances0104();

void GetObjectsWithTags0101();
void GetObjectsWithTags0102();
void GetObjectsWithTags0103();
void GetObjectsWithTags0104();
void GetObjectsWithTags0105();
void GetObjectsWithTags0106();

void GetLocalObject0101();
void GetLocalInt0101();

void GetLocalLong0101();
void GetLocalLong0102();
void GetLocalLong0103();
void GetLocalLong0104();
void GetLocalLong0106();
void GetLocalLong0107();
void GetLocalLong0108();
void GetLocalLong0109();
void GetLocalLong0110();

void GetLocalFloat0101();
void GetLocalFloat0102();
void GetLocalFloat0103();
void GetLocalFloat0104();
void GetLocalFloat0106();
void GetLocalFloat0107();
void GetLocalFloat0108();
void GetLocalFloat0109();
void GetLocalFloat0110();

void GetLocalDouble0101();
void SetLocalObject0101();
void SetLocalInt0101();
void SetLocalLong0101();
void SetLocalFloat0101();
void SetLocalDouble0101();
void SetBreakpoint0101();
void ClearBreakpoint0101();
void SetFieldAccessWatch0101();
void ClearFieldAccessWatch0101();
void SetFieldModificationWatch0101();
void ClearFieldModif0101();
void GetLoadedClasses0101();
void GetClassloaderClasses0101();
void GetClassSignature0101();
void GetClassStatus0101();
void GetSourceFileName0101();
void GetClassModifiers0101();
void GetClassMethods0101();
void GetClassFields0101();
void GetImplementedInterfaces0101();
void IsInterface0101();
void IsArrayClass0101();
void GetClassLoader0101();
void GetSourceDebugExtension0101();
void RedefineClasses0101();
void GetObjectSize0101();
void GetObjectHashCode0101();
void GetObjectMonitorUsage0101();
void GetFieldName0101();
void GetFieldDeclaringClass0101();
void GetFieldModifiers0101();
void IsFieldSynthetic0101();
void GetMethodName0101();
void GetMethodDeclaringClass0101();
void GetMethodModifiers0101();
void GetMaxLocals0101();
void GetArgumentsSize0101();
void GetLineNumberTable0101();
void GetMethodLocation0101();
void GetLocalVariableTable0101();
void GetBytecodes0101();
void IsMethodNative0101();
void IsMethodSynthetic0101();
void IsMethodObsolete0101();
void CreateRawMonitor0101();
void DestroyRawMonitor0101();
void RawMonitorEnter0101();
void RawMonitorExit0101();
void RawMonitorWait0101();
void RawMonitorNotify0101();
void RawMonitorNotifyAll0101();
void SetJNIFunctionTable0101();
void GetJNIFunctionTable0101();
void SetEventCallbacks0101();
void SetEventNotificationMode0101();
void GenerateEvents0101();
void GetExtensionFunctions0101();
void GetExtensionEvents0101();
void SetExtensionEventCallback0101();
void GetPotentialCapabilities0101();
void AddCapabilities0101();
void RelinquishCapabilities0101();
void GetCapabilities0101();
void GetCurThrCPUTimInfo0101();
void GetCurrentThreadCPUTime0101();
void GetThreadCPUTimerInfo0101();
void GetThreadCPUTime0101();
void GetTimerInfo0101();
void GetTime0101();
void GetAvailableProcessors0101();
void AddToBootCLSearch0101();
void GetSystemProperties0101();
void GetSystemProperty0101();
void SetSystemProperty0101();
void GetPhase0101();
void DisposeEnvironment0101();
void SetEnvironmentLocalStorage0101();
void GetEnvironmentLocalStorage0101();
void GetVersionNumber0101();
void GetErrorName0101();
void SetVerboseFlag0101();
void GetJLocationFormat0101();
void Breakpoint0101();
void ClassFileLoadHook0101();
void ClassLoad0101();
void ClassPrepare0101();
void CompiledMethodLoad0101();
void CompiledMethodUnload0101();
void DataDumpRequest0101();
void DynamicCodeGenerated0101();
void Exception0101();
void ExceptionCatch0101();
void FieldAccess0101();
void FieldModification0101();
void FramePop0101();
void GarbageCollectionFinish0101();
void GarbageCollectionStart0101();
void MethodEntry0101();
void MethodExit0101();
void MonitorContendedEnter0101();
void MonitorContendedEntered0101();
void MonitorWait0101();
void MonitorWaited0101();
void NativeMethodBind0101();
void ObjectFree0101();
void SingleStep0101();
void ThreadEnd0101();
void ThreadStart0101();
void VMDeath0101();
void VMInit0101();
void VMObjectAlloc0101();
void VMStart0101();

jint JNICALL special_method_NativeMethodBind0101 (JNIEnv* jni_env, jclass klass, jint a, jint b);

void JNICALL func_RunAgentThread0101(jvmtiEnv* jvmti_env, JNIEnv* jni_env, void* arg);

void JNICALL func_PopFrame0104(jvmtiEnv* jvmti_env, JNIEnv* jni_env, void* arg);

void JNICALL func_NotifyFramePop0104(jvmtiEnv* jvmti_env, JNIEnv* jni_env, void* arg);

#endif /* _FAKE_H_ */


