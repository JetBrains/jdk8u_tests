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

.interface abstract public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putstatic/putstatic21/putstatic2101/putstatic2101pTest/putstatic2101pTest
.super java/lang/Object
.field public static final testField I=111

; interface initializer
.method public static <clinit>()V
      .limit stack 1
     ldc 999
     putstatic org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putstatic/putstatic21/putstatic2101/putstatic2101pTest/putstatic2101pTest/testField I
     return
.end method
