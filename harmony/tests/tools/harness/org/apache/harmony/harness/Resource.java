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
/**
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.3 $
 */
package org.apache.harmony.harness;

class Resource {
    int    lockedBy = -1;    //free
    String name;             //name of the resource
    Object runObjByName;     //name class of the resource
    String className;        //type(class) name of the resource
    Object runObjByClassName; //type class of the resource

    public Resource(String name, Object nameObj, String cl, Object clObj) {
        this.name = name;
        runObjByName = nameObj;
        className = cl;
        runObjByClassName = clObj;
    }

    public String getName() {
        return name;
    }

    public Object getResourceClass() {
        return runObjByName;
    }

    public String getResourceTypeName() {
        return className;
    }

    public Object getResourceTypeClass() {
        return runObjByClassName;
    }
}