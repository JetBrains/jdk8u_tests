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

.class public abstract org/apache/harmony/test/func/reg/vm/btest6883/TestInstantiationError1 
.super java.lang.Object

.method public <init>()V
   .limit stack 4
   .limit locals 1
   aload_0
   invokespecial java/lang/Object/<init>()V
   aload_0
   iconst_1
   putfield org/apache/harmony/test/func/reg/vm/btest6883/TestInstantiationError1/i I
   return
.end method

