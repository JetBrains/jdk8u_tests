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
; Version: $Revision: 1.2 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/arithm/dadd/dadd01/dadd0102/dadd0102p
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
   .limit stack 4
   .limit locals 2

   ldc2_w -4606844648827964621 ; bitwise equal -4.3 double
   invokestatic java/lang/Double/longBitsToDouble(J)D

   ldc2_w -4610785298501913805 ; bitwise equal -2.4 double
   invokestatic java/lang/Double/longBitsToDouble(J)D

   dadd ; must be -6.7
   invokestatic java/lang/Double/doubleToLongBits(D)J

   ; mask last 6 bits
   ldc2_w 63
   lor

   ldc2_w -4604142489051542273 ; bitwise equal -6.7 double (with masked last 6 bits)
   lcmp
   ifne Fail
   sipush 104
   ireturn

 Fail:
   sipush 105
   ireturn

   return
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/arithm/dadd/dadd01/dadd0102/dadd0102p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/arithm/dadd/dadd01/dadd0102/dadd0102p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/arithm/dadd/dadd01/dadd0102/dadd0102p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
