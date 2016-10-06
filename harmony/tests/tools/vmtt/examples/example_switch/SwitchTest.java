/*
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
*/
public class SwitchTest {

	public void foo(int i) {
		int j = i;
		switch (i) {
		case 1:
			i++;
			i += 12;
			break;
		case 2:
			i--;
			break;
		case 3:
			i = i + 2 + i * 123;
		case 4:
			i++;
			break;
		}
		int k = i;
		switch (k) {
		case 1:
			i++;
			break;
		case 65535:
			i = i + 2 + i * 123;
		}
		
	}
}
