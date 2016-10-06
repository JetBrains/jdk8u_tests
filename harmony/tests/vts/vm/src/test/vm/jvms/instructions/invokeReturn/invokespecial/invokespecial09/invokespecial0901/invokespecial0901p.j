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
; Author: Alexander D. Shipilov
; Version: $Revision: 1.3 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial09/invokespecial0901/invokespecial0901p
.super org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial09/invokespecial0901/invokespecial0901pTest

;
; initializer
.method public <init>()V
	.limit locals 1
	.limit stack 1
	
   aload_0
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial09/invokespecial0901/invokespecial0901pTest/<init>()V
   return
.end method

;
; test method
.method public test([Ljava/lang/String;)I
   .limit stack 2
   .limit locals 2

   .catch java/lang/AbstractMethodError from first to second using catcher
 first:
   ; invoke abstTest. Must throw AbstractMethodError.
   aload_0 ; push this
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial09/invokespecial0901/invokespecial0901pTest/abstTest()V
         ; must throw java/lang/AbstractMethodError
   sipush 105
   ireturn ; return 105
 second:

 catcher:
 ; AbstractMethodError has been thrown
   sipush 104
   ireturn

.end method

; virtualTest method
.method public virtualTest()V
  .limit stack 1
  .limit locals 1

  return  
.end method


;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial09/invokespecial0901/invokespecial0901p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial09/invokespecial0901/invokespecial0901p/<init>()V
  aload_0
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial09/invokespecial0901/invokespecial0901p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
