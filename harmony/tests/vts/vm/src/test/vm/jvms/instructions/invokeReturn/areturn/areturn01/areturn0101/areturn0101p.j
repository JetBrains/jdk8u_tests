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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/areturn/areturn01/areturn0101/areturn0101p
.super java/lang/Object

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
   .limit stack 2
   .limit locals 2
   
   aload_0
   invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/areturn/areturn01/areturn0101/areturn0101p/areturnTest()Ljava/lang/String; ;must return "Hi!"
   ldc "Hi!" ;push "Hi!"
   if_acmpne Fail ;if not equivalent jump to Fail
   sipush 104
   ireturn
Fail:
   sipush 105
   ireturn

.end method

;
; areturnTest method
.method public areturnTest()Ljava/lang/String;
   .limit stack 1
   .limit locals 1

   ldc "Hi!" ;push "Hi!"
   areturn ; return "Hi!"
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/areturn/areturn01/areturn0101/areturn0101p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/areturn/areturn01/areturn0101/areturn0101p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/areturn/areturn01/areturn0101/areturn0101p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
