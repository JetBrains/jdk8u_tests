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

.source threads10101p$1.j
.class  org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads10101/threads10101p$1
.super java/lang/Thread

.field final this$0 Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads10101/threads10101p;

.method  <init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads10101/threads10101p;)V
    .limit stack 2
    .limit locals 2

    aload_0
    aload_1
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads10101/threads10101p$1/this$0 Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads10101/threads10101p;
    aload_0
    invokespecial java/lang/Thread/<init>()V
    return
.end method

.method public run()V
    .limit stack 2
    .limit locals 2

    ;                synchronized (threads10101p.this) {
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads10101/threads10101p$1/this$0 Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads10101/threads10101p;
    dup
    astore_1
    monitorenter
    ;                    v = 37;
    aload_1
    bipush 37
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads10101/threads10101p/v I
    ;                    threads10101p.this.notifyAll();
    ;                }
    aload_1
    invokevirtual java/lang/Object/notifyAll()V
    aload_1
    monitorexit ; trigger 100
    return
.end method
