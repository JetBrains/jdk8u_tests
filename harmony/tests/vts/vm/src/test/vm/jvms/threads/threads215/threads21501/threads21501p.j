;    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable
;
;    Licensed under the Apache License, Version 2.0 (the "License");
;    you may not use this file except in compliance with the License.
;    You may obtain a copy of the License at
;
;       http://www.apache.org/licenses/LICENSE-2.0
;
;    Unless required by applicable law or agreed to in writing, software
;    distributed under the License is distributed on an "AS IS" BASIS,
;    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;
;    See the License for the specific language governing permissions and
;    limitations under the License.

;
; Author: Maxim N. Kurzenev
; Version: $Revision: 1.3 $
;

.source threads21501p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/threads21501p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/timedTest
.implements org/apache/harmony/vts/test/vm/jvms/threads/share/FlaggedTest

.field volatile flag Z
.field  helper Lorg/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/Helper;

.method public <init>()V
    .limit stack 4
    .limit locals 1

    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/timedTest/<init>()V
    
    ; flag = false;
    aload_0
    iconst_0
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/threads21501p/flag Z
    
    ; helper = new Helper(this);
    aload_0
    new org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/Helper
    dup
    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/Helper/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/FlaggedTest;)V
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/threads21501p/helper Lorg/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/Helper;
    return
.end method

.method public testTimed(I)I
    .limit stack 2
    .limit locals 4

    .throws java/lang/InterruptedException

    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/threads21501p/helper Lorg/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/Helper;
    dup
    astore_2
    ; synchronized (helper) {
    monitorenter

    ; helper.start();
    aload_2
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/Helper/start()V

    ; Thread.sleep(delay);
    iload_1 ; delay parameter
    i2l
    invokestatic java/lang/Thread/sleep(J)V

    ; if (flag) {return 105;}
    ; //since Helper's run() body does not execute yet
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/threads21501p/flag Z
    ifeq FlagOK

    bipush 105
    aload_2
    monitorexit
    ireturn

FlagOK:
    ; helper.wait();
    aload_2
    invokevirtual java/lang/Object/wait()V

    ; close synchronized block
    aload_2
    monitorexit

    ; //Helper's run() body is executed
    ; return flag ? 104 : 105;
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/threads21501p/flag Z
    ifeq Failed
    bipush 104
    ireturn

Failed:
    bipush 105
    ireturn

.end method

.method public setFlag(Z)V
    .limit stack 2
    .limit locals 2

    aload_0
    iload_1
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/threads21501p/flag Z
    return

.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1
    .throws java/lang/InterruptedException

    new org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/threads21501p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/threads21501p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads215/threads21501/threads21501p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return

.end method
