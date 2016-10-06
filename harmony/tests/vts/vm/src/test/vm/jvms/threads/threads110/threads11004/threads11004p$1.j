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

.source threads11004p$1.j
.class  org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p$1
.super java/lang/Thread

.field final this$0 Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p;

.method  <init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p;)V
    .limit stack 2
    .limit locals 2

    aload_0
    aload_1
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p$1/this$0 Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p;
    aload_0
    invokespecial java/lang/Thread/<init>()V
    return
.end method

.method public run()V
    .limit stack 3
    .limit locals 1

LoopStart:
    ; while (!isInterrupted() && v < 127) {
    ;     v++;
    ;     Thread.yield();
    ; }
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p$1/isInterrupted()Z
    ifne End
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p$1/this$0 Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p;
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p/v I
    bipush 127
    if_icmpge End
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p$1/this$0 Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p;
    dup
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p/v I
    iconst_1
    iadd
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11004/threads11004p/v I
    invokestatic java/lang/Thread/yield()V ; improves failure rate for wrong implementations
    goto LoopStart
End:
    return
.end method
