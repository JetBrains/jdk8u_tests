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
; Author: Maxim N. Kurzenev
; Version: $Revision: 1.2 $
;

; This is an equivalent of the following source
; 
; public class AnnyCaller extends Caller {
;     AnnyBetty t;
; 
;     public AnnyCaller(AnnyBetty t) {
;         this.t = t;
;     }
; 
;     public void run() {
;         while (!isInterrupted()) {
;             t.anny();
;             synchronized (t) {
;                 setReady(true);
;                 try {
;                     t.wait();
;                 } catch (InterruptedException e) {
;                     interrupt();
;                 }
;             }
;         }
;     }
; }

.source AnnyCaller.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller
.super org/apache/harmony/vts/test/vm/jvms/threads/share/Caller

.field  t Lorg/apache/harmony/vts/test/vm/jvms/threads/share/AnnyBetty;

.method public <init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/AnnyBetty;)V
    .limit stack 2
    .limit locals 2

    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/Caller/<init>()V
;         this.t = t;
    aload_0
    aload_1
    putfield org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/t Lorg/apache/harmony/vts/test/vm/jvms/threads/share/AnnyBetty;
    return
.end method

.method public run()V
    .limit stack 2
    .limit locals 3

    .catch java/lang/InterruptedException from From to To using Catcher

LoopStart:
    ;         while (!isInterrupted()) {
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/isInterrupted()Z
    ifne End

    ;             t.anny();
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/t Lorg/apache/harmony/vts/test/vm/jvms/threads/share/AnnyBetty;
    invokeinterface org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyBetty/anny()V 1

    ;             synchronized (t) {
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/t Lorg/apache/harmony/vts/test/vm/jvms/threads/share/AnnyBetty;
    dup
    astore_1
    monitorenter

    ;                 setReady(true);
    aload_0
    iconst_1
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/setReady(Z)V
From:
    ;                 try {
    ;                     t.wait();
    aload_1
    invokevirtual java/lang/Object/wait()V
To:
    goto EndSynchBlock
Catcher:
    ;                 } catch (InterruptedException e) {
    ;                     interrupt();
    ;                 }
    astore_2 ; needed for stack height consistency
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/interrupt()V
EndSynchBlock:
    ;             } //synchronized
    aload_1
    monitorexit
    ;         } // while
    goto LoopStart
End:
    return
.end method
