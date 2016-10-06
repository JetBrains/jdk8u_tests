/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
package org.apache.harmony.vts.test.vm.jvmti;

/** 
 * @author Valentin Al. Sitnick, Aleksei Y. Semenov
 * @version $Revision: 1.1 $
 *
 */ 
public class MonitorContendedEntered0102 extends Thread {
    static final Object obj = new Object();
    
    static boolean first = true;
    
    
    public MonitorContendedEntered0102(String name){
    	super(name);
    }
    
    public void run(){
    	synchronized (obj) {
    		if (first) {
    			first = false;  
    			System.out.println(this.getName()+": first");
    			try {
    				Thread.sleep(1000);
    				obj.wait();
    			} catch (Throwable ignore) {
    				ignore.printStackTrace();
    			}
    		} else {
    			try {
    				obj.notifyAll();
    				Thread.sleep(1000);
    			} catch (Throwable ignore) {
    				ignore.printStackTrace();
    			}    			
    		}    		    		
    	}    	
    }
    
    
    
    static public void main(String args[]) {
        
    	new MonitorContendedEntered0102("MonitorContendedEntered0102.thread_1").start();
    	new MonitorContendedEntered0102("MonitorContendedEntered0102.thread_2").start();
    	
        return;
    }
}


