package org.punit.runner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

class StreamReaderThread extends Thread {
    private InputStream _is;
    private PrintStream _ps;

    public StreamReaderThread(InputStream is, boolean errStream) {
        _is = is;
        _ps = errStream ? System.err : System.out;
    }

    public void run() {
        InputStreamReader isr = new InputStreamReader(_is);
        BufferedReader reader = new BufferedReader(isr);
        try {
            String string = null;
            while(true) {
                string = reader.readLine();
                if(string == null) {
                    break;
                }
                _ps.println(string);
            } 
        } catch (java.io.IOException e) {
        }
    }
}
