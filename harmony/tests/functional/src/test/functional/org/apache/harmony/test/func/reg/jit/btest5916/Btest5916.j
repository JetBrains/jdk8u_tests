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

.class                   public org/apache/harmony/test/func/reg/jit/btest5916/Btest5916
.super                   org/apache/harmony/test/share/reg/RegressionTest

.method                  public <init>()V
   .limit stack          1
   .limit locals         1
   aload_0
   invokespecial         org/apache/harmony/test/share/reg/RegressionTest/<init>()V
   return
.end method              

.method                  public static main([Ljava/lang/String;)V
   .limit stack          5
   .limit locals         1
   aload_0
   arraylength
   tableswitch -2147483648
        _1
        _2
        default :        _EXIT
_1:
    getstatic             java/lang/System/err Ljava/io/PrintStream;
    ldc                   "failed - must not reach here, array length cant be negative"
    invokevirtual         java/io/PrintStream/println(Ljava/lang/String;)V
    invokestatic           org/apache/harmony/test/share/reg/RegressionTest/failed()I
    invokestatic           java/lang/System/exit(I)V
    return
_2:
    getstatic             java/lang/System/err Ljava/io/PrintStream;
    ldc                   "failed - must not reach here, array length cant be negative"
    invokevirtual         java/io/PrintStream/println(Ljava/lang/String;)V
    invokestatic           org/apache/harmony/test/share/reg/RegressionTest/failed()I
    invokestatic           java/lang/System/exit(I)V
    return
_EXIT:
    invokestatic           org/apache/harmony/test/share/reg/RegressionTest/passed()I
    invokestatic           java/lang/System/exit(I)V
    return
.throws               java/lang/Exception
.end method              

