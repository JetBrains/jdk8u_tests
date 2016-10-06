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

; Author:  Nikolay Y. Amosov
; Version: $Revision: 1.1 $
; Fri Jun 23 15:33:22 MSD 2006

.class public org/apache/harmony/vts/test/vm/jvms/classFile/limitations/variables/variables01/Variables01
.super org/apache/harmony/share/Test


.method public <init>()V
.limit stack 1
.limit locals 1
.var 0 is this Lorg/apache/harmony/vts/test/vm/jvms/classFile/limitations/variables/variables01/Variables01; from Label0 to Label1

Label0:
	aload_0
	invokespecial org/apache/harmony/share/Test/<init>()V
Label1:
	return

.end method

.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 1
.var 0 is arg0 [Ljava/lang/String; from L1 to L4
.catch java/lang/StackOverflowError from L1 to L2 using L2

L1:
	new org/apache/harmony/vts/test/vm/jvms/classFile/limitations/variables/variables01/Variables01
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/classFile/limitations/variables/variables01/Variables01/<init>()V
	invokevirtual org/apache/harmony/vts/test/vm/jvms/classFile/limitations/variables/variables01/Variables01/test()I
        goto L3
L2:
        pop
        sipush 104
L3:
	invokestatic java/lang/System/exit(I)V
L4:
	return

.end method

.method public test()I
.limit stack 1
.limit locals 65535
.var 0 is this Lorg/apache/harmony/vts/test/vm/jvms/classFile/limitations/variables/variables01/Variables01; from Label0 to Label1

Label0:
	iconst_1
	istore 65534
	bipush 104
Label1:
	ireturn

.end method
