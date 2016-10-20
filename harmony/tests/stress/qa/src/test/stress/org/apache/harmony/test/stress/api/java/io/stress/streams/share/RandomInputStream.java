/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
/**
 * @author Dmitry Vozzhaev
 * @version $Revision: 1.3 $
 */

package org.apache.harmony.test.stress.api.java.io.stress.streams.share;

import java.io.IOException;
import java.io.InputStream;

/*
 * Dec 14, 2005
 */
/**
 * Generates pseudo random, repeatable char sequence 
 */
public class RandomInputStream extends InputStream {

	long seed;
	
	public void setPos(int pos) {
		this.seed = ((long)pos ^ 0x5DEECE66DL) & ((1L << 48) - 1);
	}
	
	public int available()
	{
		return Integer.MAX_VALUE;
	}

	public int read() throws IOException {
		seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
	    return ((int)(seed >>> 12) & 0xFFFF) % 128;
	}
}
