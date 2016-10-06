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
.class public org/apache/harmony/vts/test/vm/jvms/instructions/stack/dup_x2/dup_x205/dup_x20506/dup_x20506p
.super java/lang/Object

; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method
; test method

.method public test([Ljava/lang/String;)I
  .limit stack 5
  .limit locals 5
 

 sipush 10
 ldc -1.0
 ldc 0.0
 fdiv
 ldc -1.0
 ldc 0.0
 fdiv
 ldc -1.0
 ldc 0.0
 fdiv

 dup_x2
 fstore 1
 fstore 2
 fstore 3
 fstore 4
 fload 1
 ldc -1.0
 ldc 0.0
 fdiv
 fcmpg
 iconst_0
 if_icmpne L3
 fload 2
 ldc -1.0
 ldc 0.0
 fdiv
 fcmpg
 iconst_0
 if_icmpne L3
 fload 3
 ldc -1.0
 ldc 0.0
 fdiv
 fcmpg
 iconst_0
 if_icmpne L3
 fload 4
 ldc -1.0
 ldc 0.0
 fdiv
 fcmpg
 iconst_0
 if_icmpne L3
 
 sipush  10
 if_icmpeq L4
 sipush  105
 ireturn
   
 
L4:
 sipush  104
 ireturn
  
L3:
 sipush  105
 ireturn  


.end method



;
;standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/stack/dup_x2/dup_x205/dup_x20506/dup_x20506p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/stack/dup_x2/dup_x205/dup_x20506/dup_x20506p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/stack/dup_x2/dup_x205/dup_x20506/dup_x20506p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method


















