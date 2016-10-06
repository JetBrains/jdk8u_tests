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
; Version: $Revision: 1.2 $
;

.source threads11002p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest

.field volatile v B

.method public <init>()V
    .limit stack 2
    .limit locals 1

    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest/<init>()V
    aload_0
    bipush -128
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p/v B
    return
.end method

.method public testTimed(I)I
    .limit stack 4
    .limit locals 9

    ;        boolean passed = true;
    iconst_1
    istore_2
    ;        threads11002p t = this;
    aload_0
    astore_3
    ;        Thread t1 = new threads11002p$1();
    new org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p$1
    dup
    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p$1/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p;)V
    astore 4
    ;        Interruptor killer = new Interruptor(this, delay);
    new org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor
    dup
    aload_0
    iload_1
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest;I)V
    astore 5
    ;        t1.start();
    aload 4
    invokevirtual java/lang/Thread/start()V
    ;        killer.start();
    aload 5
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/start()V

LoopStart:
    ;        while (!interrupted() && t1.isAlive()) {
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p/interrupted()Z
    ifne Cleanup
    aload 4
    invokevirtual java/lang/Thread/isAlive()Z
    ifeq Cleanup
    ;            byte v1 = v;
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p/v B
    istore 6
    ;            byte v2 = t.v;
    aload_3
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p/v B
    istore 7
    ;            byte v3 = v;
    ; // wrong implementation may retrieve here cached value stored in v1
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p/v B
    istore 8
    ;            if (v2 > v3) {
    ;                passed = false;
    ;                break;
    ;            }
    ;        }
    iload 7
    iload 8
    if_icmple LoopStart
    iconst_0
    istore_2
Cleanup:
    ;        t1.interrupt();
    aload 4
    invokevirtual java/lang/Thread/interrupt()V
    ;        return passed ? 104 : 105;
    iload_2
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

    new org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11002/threads11002p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
