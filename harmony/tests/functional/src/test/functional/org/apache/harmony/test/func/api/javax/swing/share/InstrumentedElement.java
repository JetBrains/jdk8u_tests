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
 
package org.apache.harmony.test.func.api.javax.swing.share;

import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;

public class InstrumentedElement implements Element {

    public int getElementCount() {
        InstrumentedUILog.add(new Object[] {"Element.getElementCount"} );
        return 0;
    }

    public int getEndOffset() {
        InstrumentedUILog.add(new Object[] {"Element.getEndOffset"} );
        return 0;
    }

    public int getStartOffset() {
        InstrumentedUILog.add(new Object[] {"Element.getStartOffset"} );
        return 0;
    }

    public boolean isLeaf() {
        InstrumentedUILog.add(new Object[] {"Element.isLeaf"} );
        return false;
    }

    public int getElementIndex(int arg0) {
        InstrumentedUILog.add(new Object[] {"Element.getElementIndex", "" +  arg0} );
        return 0;
    }

    public String getName() {
        InstrumentedUILog.add(new Object[] {"Element.getName"} );
        return null;
    }

    public AttributeSet getAttributes() {
        InstrumentedUILog.add(new Object[] {"Element.getAttributes"} );
        return null;
    }

    public Document getDocument() {
        InstrumentedUILog.add(new Object[] {"Element.getDocument"} );
        return null;
    }

    public Element getParentElement() {
        InstrumentedUILog.add(new Object[] {"Element.getParentElement"} );
        return null;
    }

    public Element getElement(int arg0) {
        InstrumentedUILog.add(new Object[] {"Element.getElement", "" +  arg0} );
        return null;
    }

}
