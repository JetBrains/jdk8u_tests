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

.source threads09402p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/threads09402p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/timedTest
.implements org/apache/harmony/vts/test/vm/jvms/threads/share/FlaggedTest

.field  flag Z
.field  helper Lorg/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/Helper;

.method public <init>()V
    .limit stack 4
    .limit locals 1

    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/timedTest/<init>()V
    aload_0

    ;    boolean flag = false;
    iconst_0
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/threads09402p/flag Z

    ;    Helper helper = new Helper(this);
    aload_0
    new org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/Helper
    dup
    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/Helper/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/FlaggedTest;)V
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/threads09402p/helper Lorg/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/Helper;
    return
.end method

.method public testTimed(I)I
    .limit stack 2
    .limit locals 3

    .throws java/lang/InterruptedException

    ;        synchronized (helper) {
    ;            synchronized (helper) {
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/threads09402p/helper Lorg/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/Helper;
    dup
    astore_2
    monitorenter
    aload_2
    monitorenter

    ;                helper.start();
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/threads09402p/helper Lorg/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/Helper;
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/Helper/start()V

    ;            } // close inner synchronized block
    aload_2
    monitorexit

    ;            Thread.sleep(delay);
    iload_1
    i2l
    invokestatic java/lang/Thread/sleep(J)V

    ;            if (flag) {
    ;                //Helper's inner synchronized block should not be executed yet
    ;                return 105;
    ;            }
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/threads09402p/flag Z
    ifeq FirstCheck_OK
    aload_2
    monitorexit
    bipush 105
    ireturn

FirstCheck_OK:
    ;        } // close outer synchronized block
    aload_2
    monitorexit

    ;        synchronized (this) {
    ;            //Helper's inner synchronized block should already be executed
    ;            return flag ? 104 : 105;
    ;        }
    aload_0
    monitorenter

    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/threads09402p/flag Z
    ifeq Failed
    bipush 104
    goto End
Failed:
    bipush 105
End:
    aload_0
    monitorexit
    ireturn
.end method

.method public setFlag(Z)V
    .limit stack 2
    .limit locals 2

    ;        this.flag = flag;
    aload_0
    iload_1
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/threads09402p/flag Z
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    .throws java/lang/InterruptedException

    new org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/threads09402p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/threads09402p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09402/threads09402p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
