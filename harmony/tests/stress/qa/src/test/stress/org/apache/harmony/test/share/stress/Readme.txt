Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

=== Stress Test Generator ===

Stress test generator is a tool to combine simple tests into stress tests
using a string scenario.

Scenario passes to the generator as a vm argument:
       -Dorg.apache.harmony.test.ReliabilityRunner.params="Scenario"

Scenario has syntax:
       ClassToRunName { parameters }

For example, scenario:
       -Dorg.apache.harmony.test.ReliabilityRunner.params="org.apache.harmony.test.stress.share.stress.Test1 { 100 }"
will execute Test1 with parameter "100".

Parameters are stored in a thread local storage
org.apache.harmony.test.share.stress.util.ParametersStorage. Tests receive
their parameters from storage using currentThread() as a key:
       String params = ParametersStorage.storage.get(
           java.lang.Thread.currentThread()) 

Class org.apache.harmony.test.share.stress.generator.Generator presets a
context. It is extended by two childs: Thread and Loop, which launch simple
test cases in several threads or in an infinite loop.

Class org.apache.harmony.test.share.stress.generator.Thread creates new
threads and executes a string scenario. For example, scenario:
       -Dorg.apache.harmony.test.ReliabilityRunner.params=
           "org.apache.harmony.test.share.stress.generator.Thread {
               52
               org.apache.harmony.test.stress.share.stress.Test1 {}
           }"
execute Test1 in 52 parallel threads. By default it creates one new thread.

Class org.apache.harmony.test.share.stress.generator.Loop executes a test
case in a loop. For example, scenario:
       -Dorg.apache.harmony.test.ReliabilityRunner.params="
           org.apache.harmony.test.share.stress.generator.Loop { 
           org.apache.harmony.test.stress.share.stress.Test1 {}
       }"
will start execution of Test1 in a loop. Execution of loop performs while
ReliabilityRunner.isActive() returns true.


