#!/usr/bin/perl
#
#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

use strict;

my $file = shift;

&usage() unless -f $file;

open(F, $file) || die "Can't open file $file for reading\n";
$/ = undef;
my $fileContents = <F>;
close(F);

my $CLASS = undef;

if($fileContents !~ m/\s(?:extends|implements)\s+(\S+)/) {
  die "no 'extends' or 'implements' found in file source\n";
}
$CLASS = $1;  


#ok, now instrument
while($fileContents =~ s/\s(\S+\s*\((?:.|\n)*?\))(\s*(?:throws\s[\w\,\s]+|)\s*{)(\s+)((?:return\s|super\.|\}))/ " " . $1 . $2 . $3 . &methodToLogEntry($1) . $3 . $4/ge) {
} 

print $fileContents;


sub methodToLogEntry($) {
  my ($method) = @_;
  $method =~ s/\n/ /g;

  $method =~ m/^(.*)\((.*)\)/;
  my $methodName = $1;
  my $methodArgs = $2;
  #print "name is $methodName\n";
  #print "args is $methodArgs\n";



  $methodName =~ s/\s+//g;
  my @args = split(/\,/, $methodArgs);
  foreach my $arg (@args) {
     #print "arg is $arg\n";
     $arg =~ s/^\s+//;
     $arg =~ s/\s+$//;
     #may be object or primitive type
     ($arg =~ s/^[A-Z]\S+//) || ($arg =~ s/^[a-z]\S+/"" + /);
  }
  unshift @args, "\"$CLASS.$methodName\"";

  return 'InstrumentedUILog.add(new Object[] {' . join(', ', @args) . '} );'; 
}


sub usage() {
  print "Usage: $0 <java file to instrument>\n";
  exit 1;
}
