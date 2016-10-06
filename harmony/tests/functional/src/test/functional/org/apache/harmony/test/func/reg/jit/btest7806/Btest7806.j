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
.class                   public org/apache/harmony/test/func/reg/jit/btest7806/Btest7806
.super                   org/apache/harmony/test/share/reg/RegressionTest

.method public static main([Ljava/lang/String;)V
    .limit stack 42
    .limit locals 238
    ldc "IifLQnKcUfveHupc"
    aconst_null
    sipush 32400
    fconst_2
    ldc -998103557
lastblock:
    getstatic java/lang/System/err Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(I)V
    getstatic java/lang/System/err Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(F)V
    getstatic java/lang/System/err Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(I)V
    getstatic java/lang/System/err Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(Ljava/lang/Object;)V
    getstatic java/lang/System/err Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(Ljava/lang/Object;)V
; exiting
    invokestatic           org/apache/harmony/test/share/reg/RegressionTest/passed()I
    invokestatic           java/lang/System/exit(I)V
    return
.end method
