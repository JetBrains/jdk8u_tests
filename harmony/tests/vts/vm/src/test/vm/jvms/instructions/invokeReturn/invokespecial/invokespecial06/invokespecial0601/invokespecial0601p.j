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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial06/invokespecial0601/invokespecial0601p
.super org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial06/invokespecial0601/invokespecial0601pSuper

;
; initializer
.method public <init>()V
   aload_0
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial06/invokespecial0601/invokespecial0601pSuper/<init>()V
   return
.end method

;
; test method
.method public test([Ljava/lang/String;)I

   .limit stack 7
   .limit locals 2

   
 ; invokesuper virtualTest from superclass over subclass.
   new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial06/invokespecial0601/invokespecial0601pSub
   dup
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial06/invokespecial0601/invokespecial0601pSub/<init>()V
         ; invokespecial0601pSub Object in stack (subclass object)
   ldc "Hello!" ; push "Hello!"
   iconst_3 ; push int 3
   dconst_1 ; push double 1
   lconst_1 ; push long 1
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial06/invokespecial0601/invokespecial0601pSuper/virtualTest(Ljava/lang/String;IDJ)I
         ; invokespecial superclass. Must return 104
   ireturn ; return 104

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial06/invokespecial0601/invokespecial0601p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial06/invokespecial0601/invokespecial0601p/<init>()V
  aload_0
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial06/invokespecial0601/invokespecial0601p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
