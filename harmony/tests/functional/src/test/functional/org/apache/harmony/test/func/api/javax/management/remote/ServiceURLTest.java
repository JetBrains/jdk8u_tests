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
package org.apache.harmony.test.func.api.javax.management.remote;
import javax.management.remote.JMXServiceURL;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class ServiceURLTest extends MultiCase {
    JMXServiceURL url;
    String service = "service:jmx:";
    String protocol = "protocol";
    String host = "host";
    int defaultPortNamber = 0;
    public Result testCorrectServiceURLAddressProtocolName() throws Exception {

        String ASCIIChar = null;
        char[] ASCII = new char[1];
        int charCount = 0;

        for (int i = 48; i < 123; i++) {
            try {
                if (i == 58)i = 65;
                if (i == 91)i = 97;
                
                ASCII[0] = (char) i;
                ASCIIChar = new String(ASCII);
                //log.info(charCount + " ASCII Char = " + ASCII[0]);
                charCount++;
                url = new JMXServiceURL(protocol + ASCIIChar, host, defaultPortNamber);
                
            } catch (java.net.MalformedURLException e) {
                e.printStackTrace();
                return failed("protocol do not support ASCII character - " + ASCIIChar);
            }
        }
        try {
            url = new JMXServiceURL(protocol + "+", host, 0);
            url = new JMXServiceURL(protocol + "-", host, 0);
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
            return failed("protocol do not support ASCII characters '+' or '-'");
        }
        return result();
    }

    public Result testIncorrectServiceURLAddressProtocol() throws Exception {
        
        String host = "host";
        String nonASCIIChar = null;
        char[] nonASCII = new char[1];
        int exceptionCount = 0;
        for (int i = 160; i < 256; i++) {
            try {
                nonASCII[0] = (char) i;
                nonASCIIChar = new String(nonASCII);
                url = new JMXServiceURL(protocol + nonASCIIChar, host, defaultPortNamber);
                failed("Non ASCII Char - " + nonASCII[0]);
            } catch (java.net.MalformedURLException e) {
                exceptionCount++;
                //log.info("exception number - " + exceptionCount);
            }
        }
        if (exceptionCount != 96) {
            return failed("protocol should not support non ASCII characters");
        }
        return result();
    }
    
    public Result testCorrectServiceURLAddressHostName() throws Exception {

        String[] host = new String[] {
            null,
            "localhost",
            "127.0.0.1",
            "0.0.0.0",
            "1.2.3.4",
            "255.255.255.255",
            "host",
            "HOST",
            "host.NAME",
            "[1:2:3:4:5:6:7:8]",
            "1:2:3:4:5:6:7:8",
            "1::3:4:5:6:7:8",
            "::FFFF:1.2.3.4",
            "fedc:ba98:7654:3210:FEDC:BA98:7654:3210",
            "1080:0:0:0:8:800:200C:417A",
            "[1080:0:0:0:8:800:200c:417a]",
            "ff01:0:0:0:0:0:0:43",
            "0:0:0:0:0:0:0:1",
            "0:0:0:0:0:0:0:0",
            "1080::8:800:200c:417a",
            "0:0:0:0:0:0:13.1.68.3",
            "0:0:0:0:0:FFFF:129.144.52.38",
            "::13.1.68.3",
            "::FFFF:129.144.52.38",
            "ff01::43",
            "FF00:0:0:0:0:0:0:0",
            "FF02:0:0:0:0:1:FFFF:FFFF",
            "::1",
            "::",
            "Z.Z",
            "9.Z",
            "ZZ.X",
            "77.X",
            "V1.U",
            "1E.E",
            "E-E.E",
            "E-8.E",
            "9-D.D",
            "7-6.G",
            "H",
            "H7",
            "GY",
            "G-Y",
            "Y-8", 
            "BGB",
            "YH5",
            "GT-TY",
            "F.1.H",
            "7.F.V"
            };
        for(int i = 0; i < host.length ; i++ ){
        try {
            url = new JMXServiceURL(protocol,host[i],defaultPortNamber);
        } catch (java.net.MalformedURLException e) {
            return failed("JMX Service URL dose not support host name = " + host[i]);
        }
        }
        try {
            url = new JMXServiceURL(service+"proto://");
        } catch (java.net.MalformedURLException e) {
          return failed("protocol do not support minimal service string length");
        }
        return result();
    }
    
public Result testIncorrectServiceURLAddressHostName() throws Exception {
        
        int excepCount = 0;
        
        String[] host = new String[] {
                "host$",
                "255.255.255.256",
                "-255.255.255.255",
                "1.2.3",
                "1.2.3.4.5",
                "[1.2.3.4]",
                ":2:3:4:5:6:7:8",
                "[1:2:3:4:5:6:7:8",
                "1:2:3:4:5:6:7:8]",
                "8+.H",
                "7.",
                "9.92",
                "9.9.",
                "9.9.9",
                "9.9.9.",
                "9.9.9..",
                "4565.52.33.255",
                "256.8.3.0",
                "[:]",
                "[]",
                "[1]",
                "Y+u.J",
                "[81-7]",
                "[1::2::3]",
                "[[11::]]",
                ":EsPIlCe",
                "EcLipSe:",
                "-D",
                "9.",
                ".U",
                ".",
                "+.h",
                "+1.B",
                "Y+G",
                 "+Q",
                "O-"
                };
            for(int i = 0; i < host.length ; i++ ){
            try {
                url = new JMXServiceURL(protocol, host[i], defaultPortNamber);
                fail("JMX Service URL support wrong host name = " + host[i]);
            } catch (java.net.MalformedURLException e) {
                // expexted MalformedURLException should trown
                excepCount++;
            }

        }
        if (excepCount != host.length) {
            return failed("JMX Service URL support wrong host name");
        }

        return result();
    }

    public Result testCorrectServiceURLAddressForm() throws Exception {
        String service[] = {
                "service:jmx:",
                "sErviCE:jMx:",
                "service:JMX:",
                "SERVICE:JMX:", 
                } ;
      
        for(int i = 0; i < service.length ; i++ ){
            try {
                url = new JMXServiceURL(service[i]+"proto://");
            } catch (java.net.MalformedURLException e) {
                  return failed("JMX Service URL dose not support service name = " + service[i]);

            }
        }
        return result();
    }
    public Result testIncorrectServiceURLAddressForm() throws Exception {
        int excepCount = 0;
        String service [] ={
                "lopata",
                ":",
                ":jmx", 
                "jmx:",
                ":jmx:",
                "jmx",
                "service",
                "service:",
                ":service", 
                ":service:",
                "service:jmx",
                "::service::jmx::",
                "service:jmx::",
                "service::jmx",
                ":jmx:service:", 
                "pianino" };
        
        for(int i = 0; i < service.length ; i++ ){
            try {
                url = new JMXServiceURL(service[i]);
                fail("JMX Service URL have wrong form = " + service[i]);
            } catch (java.net.MalformedURLException e) {
                // expexted MalformedURLException should trown
                excepCount++;
            }

        }
        if (excepCount != service.length) {
            return failed("JMX Service URL support wrong form");
        }
        return result();
    }

    
    public Result testIncorrectServiceURLAddressURLPath() throws Exception {

        int excepCount = 0;
        String[] path = { "incorrectpath", "@" };

        for (int i = 0; i < path.length; i++) {
            try {
                url = new JMXServiceURL(protocol, host, defaultPortNamber,
                        path[i]);
                fail("JMX Service URL support wrong URL Path= " + path[i]);
            } catch (java.net.MalformedURLException e) {
                excepCount++;
                // expexted MalformedURLException should trown
            }
        }
        if (excepCount != path.length) {
            return failed("JMX Service URL support wrong URL Path");
        }
        return result();
    }
    
    public Result testCorrectServiceURLAddressURLPath() throws Exception {

        String[] path = { ";normalpath", "/normal"  };

        for (int i = 0; i < path.length; i++) {
            try {
                url = new JMXServiceURL(protocol, host, defaultPortNamber,path[i]);
            } catch (java.net.MalformedURLException e) {
                  return failed("JMX Service URL dose not support URL Path = " + path[i]);
            }
        }
        return result();
    }
    
    public Result testIncorrectServiceURLAddressSeparator() throws Exception {
        int excepCount = 0;
        String[] incorrectSeparatos = {
                "//",
                ":",
                "/",
                ":://",
                "://:",
                ":/:/:",
                "\\"
                } ;
    for (int i = 0; i < incorrectSeparatos.length; i++) {
            try {
                url = new JMXServiceURL(service+protocol+incorrectSeparatos+host);
                fail("JMX Service URL support wrong URL Path= " + incorrectSeparatos[i]);
            } catch (java.net.MalformedURLException e) {
                excepCount++;
                // expexted MalformedURLException should trown
            }
        }
        if (excepCount != incorrectSeparatos.length) {
            return failed("JMX Service URL support wrong separatos");
        }
        return result();
    }    
    public static void main(String[] args) {
        ServiceURLTest run = new ServiceURLTest();
        System.exit(run.test(args));
    }
}
