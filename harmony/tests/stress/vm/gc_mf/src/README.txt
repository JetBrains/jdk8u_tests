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


This simple garbage collector may be used for stressing and debugging
the enumeration functionality in VM.

Gc_mf is tested for IPF and IA-32 Windows (but may require small changes for
Linux, since we haven't tried it there). 

An example command line when using gc_mf is:

    java.exe -Dgc.mf.semispaces=100 -Dgc.mf.full_collection_after=0 \
        -Xmx4m SpecApplication _200_check

This forces a GC after every allocation, and keeps the last 100 versions of the
heap for debugging.

Gc_mf can also be used without any special options, and it will behave like a
regular GC.

It works correctly for compressed vtable pointers.  For compressed references
on IPF, it is trickier, since we can't control the heap base when using malloc.
In this case, it uses a heuristic that the address returned by the first malloc
call will be close to the lowest address ever returned, so it uses that to
approximate the heap base.  This was good enough to bootstrap and debug our
initial implementation of compressed references.

As far as authors are aware of the algorithms implemented in Gc_mf are new
and haven't been published yet.
