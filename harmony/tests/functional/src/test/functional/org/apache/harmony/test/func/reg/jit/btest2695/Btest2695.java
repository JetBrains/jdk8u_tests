/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 */

/*
 * Please, note that compiled test2 class should be deleted before 
 * the test running!
*/

package org.apache.harmony.test.func.reg.jit.btest2695;

public class Btest2695 {
       public static void main( String [] args) throws Exception {
          System.err.println("Hello, World!");
          new test2().m();
       }
}

class test1  {
   void m() {
      System.err.println("test1 class");
   }
}

class test2 extends test1  {}
