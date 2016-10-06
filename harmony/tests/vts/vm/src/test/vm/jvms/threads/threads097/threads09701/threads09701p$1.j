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

.source threads09701p$1.j
.class  org/apache/harmony/vts/test/vm/jvms/threads/threads097/threads09701/threads09701p$1
.super java/lang/Thread

.field final this$0 Lorg/apache/harmony/vts/test/vm/jvms/threads/threads097/threads09701/threads09701p;

.method  <init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads097/threads09701/threads09701p;)V
    .limit stack 2
    .limit locals 2

	aload_0
	aload_1
	putfield org/apache/harmony/vts/test/vm/jvms/threads/threads097/threads09701/threads09701p$1/this$0 Lorg/apache/harmony/vts/test/vm/jvms/threads/threads097/threads09701/threads09701p;
	aload_0
	invokespecial java/lang/Thread/<init>()V
	return
.end method

.method public run()V
    .limit stack 3
    .limit locals 3

    .catch java/lang/InterruptedException from L1 to L2 using Catcher

    ;                synchronized (threads09701p.this) {
    ;                    while (!isInterrupted()) {
    ;                        a++;
    ;                        threads09701p.this.notifyAll();
    ;                        try {
    ;                            threads09701p.this.wait();
    ;                        } catch (InterruptedException e) {
    ;                            interrupt();
    ;                        }
    ;                    }
    ;                }
    aload_0
	getfield org/apache/harmony/vts/test/vm/jvms/threads/threads097/threads09701/threads09701p$1/this$0 Lorg/apache/harmony/vts/test/vm/jvms/threads/threads097/threads09701/threads09701p;
	dup
	astore_1
	monitorenter
LoopStart:
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads097/threads09701/threads09701p$1/isInterrupted()Z
	ifne Cleanup

	aload_1
	dup
	getfield org/apache/harmony/vts/test/vm/jvms/threads/threads097/threads09701/threads09701p/a I
	iconst_1
	iadd
	putfield org/apache/harmony/vts/test/vm/jvms/threads/threads097/threads09701/threads09701p/a I
	aload_1
	invokevirtual java/lang/Object/notifyAll()V
L1:
	aload_1
	invokevirtual java/lang/Object/wait()V
L2:
	goto LoopStart
Catcher:
	astore_2
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads097/threads09701/threads09701p$1/interrupt()V
	goto LoopStart
Cleanup:
	aload_1
	monitorexit
	return
.end method
