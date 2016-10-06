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

package org.apache.harmony.test.func.api.java.text.MessageFormat;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import org.apache.harmony.test.func.api.java.text.share.framework.TextTestFramework;

/**
 */
public class MessageFormatTest extends TextTestFramework {

    public String[] getDatePatterns() {
        String[] result = {
                "date",
                "short",
                "medium",
                "long",
                "full",
                "G/E/aaaa/zz/, ww/WW/DD/d/FF/HHH/kkkk/KK/hhhh/m/ss/SSSS/, y ",
                "GG/EEE/aaaa/z/, www/W/DDDD/dd/F/HH/kk/KK/h/m/sss/S/, yy ",
                "GGG/EEEE/a/z/, w/W/D/dddd/FF/H/kk/KKK/hh/m/sss/SSSS/, yyy ",
                "GGG/EEEEE/a/zzz/, ww/WW/DD/dd/FF/H/k/KKK/hh/mm/ss/SS/, yyyy ",
                "GG/EEEE/a/zz/, ww/W/DDDD/dd/FF/HH/kk/KK/hhh/mmm/ss/S/, yyyyy ",
                "GGG/E/a/zzzz/, w/WWW/DDDD/dd/FFFF/HHH/k/KKK/hh/m/ss/SSS/, M ",
                "GGGG/EE/aa/zzz/, wwww/WW/DD/ddd/FFF/HH/kk/KKK/hhh/mmm/sss/SSS/, MM ",
                "G/EEEEE/aaa/zz/, www/W/D/dd/FF/HH/k/KK/hhh/mmm/sss/SS/, MMM ",
                "G/EE/aaa/zz/, wwww/WWW/D/dd/FF/H/kkkk/KKK/hhhh/mm/sss/SSS/, MMMM ",
                "G/E/aaaa/z/, www/WWW/DD/dd/FF/H/kk/KK/hhh/mmm/ssss/SSSS/, MMMMM ",
                "GG/EEE/aaa/zzz/, ww/WWWW/D/dddd/FF/HHH/kkk/KK/hhh/mmm/sss/SS/, Z ",
                "GGG/EE/a/zzz/, ww/W/DD/d/FFFF/HH/kkk/KKKK/hhh/mm/sss/SSS/, y ",
                "GG/EEE/aa/zzzz/, ww/WW/DD/ddd/FF/HH/kk/KKKK/hh/mm/s/SSS/, yy ",
                "GG/EE/a/zz/, w/WWW/DDD/dddd/F/HHH/kkk/KKK/hh/mm/ss/S/, yyy ",
                "G/EE/aaa/zz/, w/W/D/dddd/F/HHHH/kk/KKK/h/mmm/sss/SSS/, yyyy ",
        };

        return result;
    }

    private void date() {
        Random rnd = new Random();

        String[] datePatternText = {
                "G", "E", "a", "z"
        };
        String[] datePatternNumbers = {
                "w", "W", "D", "d", "F", "H", "k", "K", "h", "m", "s", "S"
        };
        String[] datePatternRaw = {
                "y", "yy", "yyy", "yyyy", "yyyyy", "M", "MM", "MMM", "MMMM",
                "MMMMM", "Z"
        };
        int count = 15;

        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            String text = "";
            for (int j = 0; j < datePatternText.length; j++) {
                String textPiece = datePatternText[j];
                for (int k = 0; k <= Math.abs(rnd.nextInt()) % 5; k++)
                    text += textPiece;
                text += "/";
            }
            String number = "";
            for (int j = 0; j < datePatternNumbers.length; j++) {
                String numberPiece = datePatternNumbers[j];
                for (int k = 0; k <= Math.abs(rnd.nextInt()) % 4; k++)
                    number += numberPiece;
                number += "/";
            }
            String raw = datePatternRaw[i % datePatternRaw.length] + " ";

            result[i] = text + ", " + number + ", " + raw;
            log.info("\"pattern, " + result[i] + "\",");
        }
    }

    private String[] getMessagePatterns() {
        String[] result = {
                "text here {0, time,short} and here",
                "text here {0, time,medium} and here",
                "text here {0, time,long} and here",
                "text here {0, time,full} and here",
                "text here {0, date,short} and here",
                "text here {0, date,medium} and here",
                "text here {0, date,long} and here",
                "text here {0, date,full} and here",
                "text here {0, date,G/E/aaaa/zz/, ww/WW/DD/d/FF/HHH/kkkk/KK/hhhh/m/ss/SSSS/, y } and here",
                "text here {0, date,GG/EEE/aaaa/z/, www/W/DDDD/dd/F/HH/kk/KK/h/m/sss/S/, yy } and here",
                "text here {0, date,GGG/EEEE/a/z/, w/W/D/dddd/FF/H/kk/KKK/hh/m/sss/SSSS/, yyy } and here",
                "text here {0, date,GGG/EEEEE/a/zzz/, ww/WW/DD/dd/FF/H/k/KKK/hh/mm/ss/SS/, yyyy } and here",
                "text here {0, date,GG/EEEE/a/zz/, ww/W/DDDD/dd/FF/HH/kk/KK/hhh/mmm/ss/S/, yyyyy } and here",
                "text here {0, date,GGG/E/a/zzzz/, w/WWW/DDDD/dd/FFFF/HHH/k/KKK/hh/m/ss/SSS/, M } and here",
                "text here {0, date,GGGG/EE/aa/zzz/, wwww/WW/DD/ddd/FFF/HH/kk/KKK/hhh/mmm/sss/SSS/, MM } and here",
                "text here {0, date,G/EEEEE/aaa/zz/, www/W/D/dd/FF/HH/k/KK/hhh/mmm/sss/SS/, MMM } and here",
                "text here {0, date,G/EE/aaa/zz/, wwww/WWW/D/dd/FF/H/kkkk/KKK/hhhh/mm/sss/SSS/, MMMM } and here",
                "text here {0, date,G/E/aaaa/z/, www/WWW/DD/dd/FF/H/kk/KK/hhh/mmm/ssss/SSSS/, MMMMM } and here",
                "text here {0, date,GG/EEE/aaa/zzz/, ww/WWWW/D/dddd/FF/HHH/kkk/KK/hhh/mmm/sss/SS/, Z } and here",
                "text here {0, date,GGG/EE/a/zzz/, ww/W/DD/d/FFFF/HH/kkk/KKKK/hhh/mm/sss/SSS/, y } and here",
                "text here {0, date,GG/EEE/aa/zzzz/, ww/WW/DD/ddd/FF/HH/kk/KKKK/hh/mm/s/SSS/, yy } and here",
                "text here {0, date,GG/EE/a/zz/, w/WWW/DDD/dddd/F/HHH/kkk/KKK/hh/mm/ss/S/, yyy } and here",
                "text here {0, date,G/EE/aaa/zz/, w/W/D/dddd/F/HHHH/kk/KKK/h/mmm/sss/SSS/, yyyy } and here",
                "text here {0, number,integer} and here",
                "text here {0, number,percent} and here",
                "text here {0, number,currency} and here",
                "text here {0, choice, 0<less zero|1#zero to one|1.0<1+} and here",
        };
        return result;
    }

    private void message() {
        String[][] elements = {
                {
                        "time", "short", "medium", "long", "full"
                }, getDatePatterns(), {
                        "number", "integer", "percent", "currency"
                }, {
                        "choice", " 0<less zero|1#zero to one|1.0<1+"
                },
        };
        for (int i = 0; i < elements.length; i++) {
            String elementMessage = "\"text here {0, " + elements[i][0] + ",";
            for (int j = 1; j < elements[i].length; j++)
                log.info(elementMessage + elements[i][j]
                        + "} and here\",");
        }
    }

    public String generateGetter(String fieldName, Object values, Class type) {

        String getterName = "get" + Character.toUpperCase(fieldName.charAt(0))
                + fieldName.substring(1);

        fieldName = Character.toLowerCase(fieldName.charAt(0))
                + fieldName.substring(1);

        //log.info(getterName);

        String typeDecl = getTypeDecl(values.getClass());

        //log.info(typeDecl);

        String valueDef = "";
        if (values.getClass().isArray()) {
            valueDef = "{";

            for (int i = 0; i < Array.getLength(values); i++) {
                valueDef += getObjectDef(Array.get(values, i), type);
                if (i != Array.getLength(values) - 1)
                    valueDef += ", ";
            }
            valueDef += "};";
        } else {
            valueDef = getObjectDef(values, type) + ";";
        }
        String methodDecl = "public " + typeDecl + " " + getterName + "(){\n"
                + "\t" + typeDecl + " " + fieldName + " = " + valueDef + "\n"
                + "\treturn " + fieldName + ";\n}\n" + typeDecl + " "
                + fieldName + " = " + getterName + "();\n";

        //log.info(methodDecl);

        return methodDecl;
    }

    public String getObjectDef(Object obj, Class type) {
        if (obj == null) {
            return null;
        } else if (type.equals(String.class)) {
            return "\"" + ((String) obj).replaceAll("\"", "\\\\\"") + "\"";
        } else if (type.isPrimitive()) {
            if (type.equals(char.class)) {
                return "'" + obj.toString().replaceAll("'", "\\'") + "'";
            } else if (type.equals(boolean.class)) {
                return (((Boolean) obj).booleanValue() ? "true" : "false");
            } else if (type.equals(long.class)) {
                return obj.toString() + "l";
            } else {
                return obj.toString();
            }
        } else {
            return "new " + getTypeDecl(obj.getClass()) + "(" + obj.toString()
                    + ")";
        }

    }

    public String getTypeDecl(Class cls) {
        String name = cls.getName();
        if (!cls.isArray())
            return name;
        int depth = 0;
        while (name.charAt(depth) == '[')
            depth++;
        name = name.substring(depth);
        String type;
        switch (name.charAt(0)) {
        case 'Z':
            type = "boolean";
            break;
        case 'B':
            type = "byte";
            break;
        case 'C':
            type = "char";
            break;
        case 'D':
            type = "double";
            break;
        case 'F':
            type = "float";
            break;
        case 'I':
            type = "int";
            break;
        case 'J':
            type = "long";
            break;
        case 'S':
            type = "short";
            break;
        case 'L':
            type = name.substring(1, name.length() - 1);
            break;
        default:
            type = "void";
            break;
        }
        type += ' ';
        while (depth-- > 0)
            type += "[]";

        return type;
    }

    public Object[] getDates() {
        return new Object[] {
                new Date(60893622875000l), new Date(-2154607920000l),
                new Date(-17961306179000l), new Date(61089174963000l),
                new Date(64068866104000l), new Date(44439656400000l),
                new Date(60893622875000l), new Date(-2154607920000l),
                new Date(-17961306179000l), new Date(61089174963000l),
                new Date(64068866104000l), new Date(44439656400000l),
                new Date(60893622875000l), new Date(-2154607920000l),
                new Date(-17961306179000l), new Date(61089174963000l),
                new Date(64068866104000l), new Date(44439656400000l),
                new Date(60893622875000l), new Date(-2154607920000l),
                new Date(-17961306179000l), new Date(61089174963000l),
                new Date(64068866104000l), new Double(3.14159),
                new Double(0.0112), new Double(Math.log(2005)),
                new Double(3.14159), new Double(0.0112),

        };
    }

    public java.lang.String [] getResults() {
        java.lang.String [] results = {
                "text here 7:14 AM and here",
                "text here 9:48:00 AM and here",
                "text here 12:17:01 UTC PM and here",
                "text here 3:16:03 o'clock PM UTC and here",
                "text here 05/04/00 and here",
                "text here 27-Mar-3378 and here",
                "text here August 23, 3899 and here",
                "text here Sunday, September 22, 1901 and here",
                "text here AD/Thu/PM/UTC/, 43/04/295/21/03/012/0012/00/0012/17/01/0000/, 00  and here",
                "text here AD/Fri/PM/UTC/, 044/1/0307/03/1/15/15/03/3/16/003/0/, 05  and here",
                "text here AD/Wednesday/PM/UTC/, 15/2/96/0005/01/19/19/007/07/15/004/0000/, 00  and here",
                "text here AD/Friday/PM/UTC/, 13/04/86/27/04/21/21/009/09/00/00/00/, 3378  and here",
                "text here AD/Wednesday/AM/UTC/, 34/4/0235/23/04/07/07/07/007/014/35/0/, 03899  and here",
                "text here AD/Sun/AM/Coordinated Universal Time/, 39/004/0265/22/0004/009/9/009/09/48/00/000/, 9  and here",
                "text here AD/Thu/PM/UTC/, 0043/04/295/021/003/12/12/000/012/017/001/000/, 10  and here",
                "text here AD/Friday/PM/UTC/, 044/1/307/03/01/15/15/03/003/016/003/00/, Nov  and here",
                "text here AD/Wed/PM/UTC/, 0015/002/96/05/01/19/0019/007/0007/15/004/000/, April  and here",
                "text here AD/Fri/PM/UTC/, 013/004/86/27/04/21/21/09/009/000/0000/0000/, March  and here",
                "text here AD/Wed/AM/UTC/, 34/0004/235/0023/04/007/007/07/007/014/035/00/, +0000  and here",
                "text here AD/Sun/AM/UTC/, 39/4/265/22/0004/09/009/0009/009/48/000/000/, 01  and here",
                "text here AD/Thu/PM/Coordinated Universal Time/, 43/04/295/021/03/12/12/0000/12/17/1/000/, 00  and here",
                "text here AD/Fri/PM/UTC/, 44/001/307/0003/1/015/015/003/03/16/03/0/, 05  and here",
                "text here AD/Wed/PM/UTC/, 15/2/96/0005/1/0019/19/007/7/015/004/000/, 4000  and here",
                "text here 3 and here", "text here 1% and here",
                "text here $7.60 and here", "text here 1+ and here"
        };
        return results;
    }

    public int fail(String arg0) {
        return super.fail(arg0);
    }

    private int format() {
        Locale.setDefault(Locale.CANADA);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Object[] dates = getDates();
        String[] patterns = getMessagePatterns();
        int count = patterns.length;
        java.lang.String [] results = getResults();
        boolean failed = false;
        for (int j = 0; j < count; j++) {
            try {
                log.info("Pattern:'" + patterns[j] + "'\n"
                        + "Object:'" + dates[j] + "'");
                MessageFormat fmt = new MessageFormat(patterns[j]);
                String result = fmt.format(new Object[] {
                    dates[j % dates.length]
                });

                if (!result.equals(results[j]))
                    failed = true;
                
                result = MessageFormat.format(patterns[j], new Object[] {
                    dates[j % dates.length]
                });
                
                if (!result.equals(results[j]))
                    failed = true;
                
                log.info("Result:   '" + result + "'\n" + "Expected: '"
                        + results[j] + "'\n\n");
            } catch (Throwable e) {
                log.info(e.getMessage());
            }
        }
        //log.info(generateGetter("results", results, String.class));
        return failed ? fail("Testing failed. Look above for details.")
                : pass();
        //log.info(generateGetter("dates", parsed, long.class));
    }

    public int test() {
        try {
            int result = format();
            return result;
        } catch (Throwable e) {
            String message = e.getMessage();
            return fail(e.toString() + " was thrown\n" + message);
        }
    }

    public static void main(String[] args) {
        System.exit(new MessageFormatTest().test());
    }
}