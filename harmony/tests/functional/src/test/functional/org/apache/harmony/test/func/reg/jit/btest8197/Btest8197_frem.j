;  Licensed to the Apache Software Foundation (ASF) under one or more
;  contributor license agreements.  See the NOTICE file distributed with
;  this work for additional information regarding copyright ownership.
;  The ASF licenses this file to You under the Apache License, Version 2.0
;  (the "License"); you may not use this file except in compliance with
;  the License.  You may obtain a copy of the License at
;
;     http://www.apache.org/licenses/LICENSE-2.0
;
;  Unless required by applicable law or agreed to in writing, software
;  distributed under the License is distributed on an "AS IS" BASIS,
;  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;  See the License for the specific language governing permissions and
;  limitations under the License.
;
;
;

.class public org/apache/harmony/test/func/reg/jit/btest8197/Btest8197_frem

.super java/lang/Object


.method public static main([Ljava/lang/String;)V
    .limit stack 89
    .limit locals 37
    
    fconst_1
    fneg
    fconst_1
    fneg
    frem

    invokestatic java/lang/Float/floatToIntBits(F)I
    getstatic java/lang/System/err Ljava/io/PrintStream;
    dup_x1
    pop
    invokevirtual java/io/PrintStream/println(I)V ; 
        
    return
.end method
