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
; @author Mikhail Bolotov
; @version $Revision: 1.2 $
;   
.class public org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0809/initialization0809p
.super java/lang/Object

.field static i I

.method static <clinit>()V
.limit stack 1
.limit locals 0

	bipush 104
	putstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0809/initialization0809p/i I
;
;	System/exit(104);
;
	getstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0809/initialization0809p/i I
	invokestatic java/lang/System/exit(I)V
	return
.end method


.method public static main([Ljava/lang/String;)V
.throws java/lang/Exception
.limit stack 1
.limit locals 1
;
;	must not be here
;
	bipush 105
	invokestatic java/lang/System/exit(I)V
	return
.end method
