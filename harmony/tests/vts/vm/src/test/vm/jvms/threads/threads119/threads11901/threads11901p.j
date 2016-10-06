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
; Version: $Revision: 1.1 $
;

.source threads11901p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads119/threads11901/threads11901p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest

.field  a I

.method public <init>()V
    .limit stack 2
    .limit locals 1

    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest/<init>()V
    aload_0
    iconst_0
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads119/threads11901/threads11901p/a I
    return
.end method

.method public testTimed(I)I
    .limit stack 4
    .limit locals 4

    .throws java/lang/InterruptedException

    ;        int i = 0;
    iconst_0
    istore_2
    ;        boolean passed = true;
    iconst_1
    istore_3
    ;        new Interruptor(this, delay).start();
    new org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor
    dup
    aload_0
    iload_1
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest;I)V
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/start()V
LoopStart:
    ;        while (!interrupted()) {
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads119/threads11901/threads11901p/interrupted()Z
    ifne Cleanup
    ;            synchronized (this) {
    aload_0
    monitorenter
    ;                a = ++i;
    aload_0
    iinc 2 1
    iload_2
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads119/threads11901/threads11901p/a I
    ;            } // unlock a lock to trigger store operation
    aload_0
    monitorexit
    ;            synchronized (this) {
    aload_0
    monitorenter
    ;                if (a != i) {
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads119/threads11901/threads11901p/a I
    iload_2
    if_icmpeq Continue
    ;                    // local copy should already be equal to i
    ;                    passed = false;
    ;                    break;
    ;                }
    ;            }
    ;        }
    ;        return passed ? 104 : 105;
    iconst_0
    istore_3
    aload_0
    monitorexit
    goto Cleanup
Continue:
    aload_0
    monitorexit
    goto LoopStart
Cleanup:
    iload_3
    ifeq Failed
    bipush 104
    ireturn
Failed:
    bipush 105
    ireturn
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    .throws java/lang/InterruptedException

    new org/apache/harmony/vts/test/vm/jvms/threads/threads119/threads11901/threads11901p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads119/threads11901/threads11901p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads119/threads11901/threads11901p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
