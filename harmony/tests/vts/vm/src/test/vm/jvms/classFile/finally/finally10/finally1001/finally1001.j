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
; Version: $Revision: 1.3 $
;

.class public org/apache/harmony/vts/test/vm/jvms/classFile/finally/finally10/finally1001/finally1001
.super java/lang/Object
.field public static testFieldInt I
.field public static testFieldInt1 I

; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method
; test method

.method public test([Ljava/lang/String;)I
   .limit stack 2
   .limit locals 4

   	sipush 0
	istore 2
    
 	jsr J1 			   
           
    iconst_3
    iload 2
    if_icmpeq R3
    sipush 105      
    ireturn		
R3:
    sipush 104      
    ireturn				               
J1:      
     
    astore_1       
    iinc 2 1    
       
    jsr S1                     
    iinc 2 1
    ret 1      
       
S1:
    astore_3
    iinc 2 1
    ret 3
                
.end method


;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1
  new org/apache/harmony/vts/test/vm/jvms/classFile/finally/finally10/finally1001/finally1001
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/classFile/finally/finally10/finally1001/finally1001/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/classFile/finally/finally10/finally1001/finally1001/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method


















