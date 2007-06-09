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
; Author: Ilia A. Leviev
; Version: $Revision: 1.1.1.1 $
;
.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1310/invokevirtual1310n
.super org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1310/invokevirtual1310super2/invokevirtual1310super2

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1310/invokevirtual1310super2/invokevirtual1310super2/<init>()V
   return
.end method

;
; test method
.method public test()I
   .limit stack 2
   .limit locals 2
      
   new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1310/invokevirtual1310super/invokevirtual1310super
   dup
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1310/invokevirtual1310super/invokevirtual1310super/<init>()V
   ;invoke protected method
   invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1310/invokevirtual1310super/invokevirtual1310super/protectedTest()I        
   ireturn 
.end method

