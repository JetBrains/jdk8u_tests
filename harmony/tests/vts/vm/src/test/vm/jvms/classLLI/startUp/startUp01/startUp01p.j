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
; Author: Mikhail Bolotov
; Version: $Revision: 1.2 $
;

.class public org/apache/harmony/vts/test/vm/jvms/classLLI/startUp/startUp01p
.super java/lang/Object

; 
;another main - should not be executed
.method public static main(Ljava/lang/String;)V 
   .limit locals 1
   bipush 105
   invokestatic java/lang/System/exit(I)V

   return
.end method

;constructor should not be executed
.method public <init>()V
   aload 0
   invokespecial java/lang/Object/<init>()V
   
   bipush 105
   invokestatic java/lang/System/exit(I)V
   return
.end method

;constructor should not be executed
.method public <init>([Ljava/lang/String;)V
   .limit locals 2
   aload 0
   invokespecial java/lang/Object/<init>()V

   bipush 105
   invokestatic java/lang/System/exit(I)V

   return
.end method


;
; the only main function is invoked by VM
.method public static main([Ljava/lang/String;)V
  .limit locals 2 	
  .limit stack 2
  
  sipush 105

; Cause linking additional classes
  invokestatic org/apache/harmony/vts/test/vm/jvms/classLLI/startUp/startUp01pTest.test()I

  invokestatic java/lang/System/exit(I)V

  return
.end method
