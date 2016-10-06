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

.source threads06501p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest

.field  A I
.field  B I
.field  C I

.method public <init>()V
    .limit stack 1
    .limit locals 1

    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest/<init>()V
    return
.end method

.method public testTimed(I)I
    .limit stack 4
    .limit locals 2

    .throws java/lang/InterruptedException

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
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/interrupted()Z
    ifne Passed
    ;            A = 1;
    ;            B = 2;
    ;            C = 3;
    aload_0
    iconst_1
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/A I
    aload_0
    iconst_2
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/B I
    aload_0
    iconst_3
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/C I
    ;            C = B;
    ;            B = A;
    ;            A = C;
    aload_0
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/B I
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/C I
    aload_0
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/A I
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/B I
    aload_0
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/C I
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/A I
    ;            if (A != 2 || B != 1 || C != 2) {
    ;                return 105;
    ;            }
    ;        }
    ;        return 104;
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/A I
    iconst_2
    if_icmpne Failed
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/B I
    iconst_1
    if_icmpne Failed
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/C I
    iconst_2
    if_icmpeq LoopStart
Failed:
    bipush 105
    ireturn
Passed:
    bipush 104
    ireturn
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    .throws java/lang/InterruptedException

    new org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads065/threads06501/threads06501p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
