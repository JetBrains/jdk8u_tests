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
; Author: Maxim V. Makarov
; Version: $Revision: 1.2 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804p
.super java/lang/Object
.implements org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804pInterface

.field public testField Lorg/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804pInterface;

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method 

;
; test method
.method public test([Ljava/lang/String;)I
    .limit locals 3
    .limit stack 3

    .catch java/lang/ClassCastException from L1 to L2 using Handler

L1:

	;
	;	Create a new instance of checkcast0804pInterface and store it in local var 2
	;				testField = new checkcast0804p()
	;
	aload_0
	new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804p
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804p/<init>()V
	putfield org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804p/testField Lorg/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804pInterface;
	aload_0
	getfield org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804p/testField Lorg/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804pInterface;
	
	;checkcast0804pInterface t = (checkcast0804pInterface)testFeld
	;
	checkcast org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804pInterface

    ; test passed
    sipush 104
    ireturn

L2:
Handler:

    ; test failed
    sipush 105
    ireturn

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1
  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/checkcast/checkcast08/checkcast0804/checkcast0804p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
