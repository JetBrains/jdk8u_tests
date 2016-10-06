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

.source threads07601p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads076/threads07601/threads07601p
.super java/lang/Object

.field public v I
.field public passed Z

.method public <init>()V
    .limit stack 1
    .limit locals 1

	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public test([Ljava/lang/String;)I
    .limit stack 3
    .limit locals 4

    .throws java/lang/InterruptedException

    ;   Thread t1 = new threads07601p$1();
	new org/apache/harmony/vts/test/vm/jvms/threads/threads076/threads07601/threads07601p$1
	dup
	aload_0
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads076/threads07601/threads07601p$1/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads076/threads07601/threads07601p;)V
	astore_2
	
    ;   Thread t2 = new threads07601p$2();
    new org/apache/harmony/vts/test/vm/jvms/threads/threads076/threads07601/threads07601p$2
	dup
	aload_0
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads076/threads07601/threads07601p$2/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads076/threads07601/threads07601p;)V
	astore_3
	
    ;   t1.start();
    ;   t1.join();
    aload_2
	invokevirtual java/lang/Thread/start()V
	aload_2
	invokevirtual java/lang/Thread/join()V
	
    ;   t2.start();
    ;   t2.join();
    aload_3
	invokevirtual java/lang/Thread/start()V
	aload_3
	invokevirtual java/lang/Thread/join()V
	
    ;   return passed ? 104 : 105;
    aload_0
	getfield org/apache/harmony/vts/test/vm/jvms/threads/threads076/threads07601/threads07601p/passed Z
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

	new org/apache/harmony/vts/test/vm/jvms/threads/threads076/threads07601/threads07601p
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads076/threads07601/threads07601p/<init>()V
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads076/threads07601/threads07601p/test([Ljava/lang/String;)I
	invokestatic java/lang/System/exit(I)V
	return
.end method
