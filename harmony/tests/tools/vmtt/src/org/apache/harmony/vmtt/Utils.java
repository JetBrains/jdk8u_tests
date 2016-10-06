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
/*
 * Created on 03.11.2004
 */

package org.apache.harmony.vmtt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.harmony.vmtt.ir.constants.*;

import org.apache.harmony.vmtt.ir.ClassFile;

/**
 * @author agolubit
 */

public class Utils implements ErrorMessages {
	
	private final static String hexSymbols = "0123456789ABCDEF";
	private final static String octalSymbols = "01234567";
	public static String defaultHexPrefix = "x";
	public static String defaultOctalPrefix = "O";
	public static char defaultQuotationSymbol = '"';


	public static String decToHex(int i) {
		return decToHex(i, defaultHexPrefix, false);
	}
	
	public static String decToHex(int i, boolean isCode) {
		return decToHex(i, defaultHexPrefix, isCode);
	}
	
	public static String decToHex(int i, String prefix, boolean isCode) {
		if (isCode) {
			i = i & 0xFF;
		}
		StringBuffer hex = new StringBuffer(Integer.toHexString(i).toUpperCase());
		if (hex.length() < 2) {
			hex.insert(0, '0');
		}
		hex.insert(0, prefix);
		return hex.toString();
	}
	
	public static int hexToDec(String hex) 
		throws NumberFormatException {

		return hexToDec(hex, defaultHexPrefix);
	}
	
	public static int hexToDec(String hex, String prefix)
		throws NumberFormatException {
		if (!isHex(hex)) {
			throw new NumberFormatException(quote(hex) + errs[E_NOT_HEX]);
		}
		if (prefix.length() > 0) {
			hex = hex.substring(prefix.length(), hex.length());
		}

		int decimal = 0;
		hex = hex.toUpperCase();
		for (int i = 0, ci = 0; i < hex.length(); i++) {
			ci = hexSymbols.indexOf(hex.charAt(i));
			decimal += ci * (int) Math.pow(16, hex.length() - 1 - i);
		}
		return decimal;
	}
	
	public static long hexToLong(String hex) 
		throws NumberFormatException {
		return hexToLong(hex, defaultHexPrefix);
	}
	
	public static long hexToLong(String hex, String prefix)
		throws NumberFormatException {
		if (!isLongHex(hex)) {
			throw new NumberFormatException(quote(hex) + errs[E_NOT_HEX]);
		}
		if (prefix.length() > 0) {
			hex = hex.substring(prefix.length(), hex.length()-1);
		}

		int ldecimal = 0;
		hex = hex.toUpperCase();
		for (int i = 0, ci = 0; i < hex.length(); i++) {
			ci = hexSymbols.indexOf(hex.charAt(i));
			ldecimal += ci * (int) Math.pow(32, hex.length() - 1 - i);
		}
		return ldecimal;
	}
	
	public static boolean isHex(String hex) {
		return isHex(hex, defaultHexPrefix);
	}
	
	public static boolean isHex(String hex, String prefix) {
		String regexp = prefix.toUpperCase() + "[0123456789ABCDEF]+";
		return Pattern.compile(regexp).matcher(hex.toUpperCase()).matches();
	}

	public static boolean isLongHex(String hex) {
		return isLongHex(hex, defaultHexPrefix);
	}

	public static boolean isLongHex(String hex, String prefix) {
		String regexp = prefix.toUpperCase() + "[0123456789ABCDEF]+[lL]";
		return Pattern.compile(regexp).matcher(hex.toUpperCase()).matches();
	}
	
	public static boolean isOctal(String oct) {
		return isHex(oct, defaultOctalPrefix);
	}
	
	public static boolean isOctal(String oct, String prefix) {
		String regexp = prefix.toUpperCase() + "[01234567]+";
		return Pattern.compile(regexp).matcher(oct.toUpperCase()).matches();
	}
	
	public static boolean isLongOctal(String oct) {
		return isLongHex(oct, defaultOctalPrefix);
	}

	public static boolean isLongOctal(String oct, String prefix) {
		if (oct.length() == 0) {
			return false;
		}
		oct = oct.toUpperCase();
		if (!oct.endsWith ("L")) return false;
		oct = oct.substring(prefix.length(), oct.length()-1);
		for (int i = 0; i < oct.length(); i++) {
			if (octalSymbols.indexOf(oct.charAt(i)) == -1) {
				return false;
			}
		}
		return true;
	}

	public static int octalToDec(String oct) 
		throws NumberFormatException {

	return octalToDec(oct, defaultOctalPrefix);
	}

	public static int octalToDec(String oct, String prefix)
		throws NumberFormatException {

		if (!isOctal(oct)) {
			throw new NumberFormatException(quote(oct) + " cannot be parsed as octadecimal number");
		}
		if (prefix.length() > 0) {
			oct = oct.substring(prefix.length(), oct.length());
	}

		int decimal = 0;
		oct = oct.toUpperCase();
		for (int i = 0, ci = 0; i < oct.length(); i++) {
			ci = octalSymbols.indexOf(oct.charAt(i));
			decimal += ci * (int) Math.pow(16, oct.length() - 1 - i);
		}
	return decimal;
	}

	public static long octalToLong(String oct) 
		throws NumberFormatException {

		return octalToLong(oct, defaultOctalPrefix);
	}

	public static long octalToLong(String oct, String prefix)
		throws NumberFormatException {

		if (!isLongHex(oct)) {
		throw new NumberFormatException(quote(oct) + " cannot be parsed as long octadecimal number");
		}
		if (prefix.length() > 0) {
			oct = oct.substring(prefix.length(), oct.length()-1);
		}

		int ldecimal = 0;
		oct = oct.toUpperCase();
		for (int i = 0, ci = 0; i < oct.length(); i++) {
			ci = octalSymbols.indexOf(oct.charAt(i));
			ldecimal += ci * (int) Math.pow(32, oct.length() - 1 - i);
		}
		return ldecimal;
	}
	
	public static String quote(String str, char quotation_symbol) {
		return quotation_symbol + str + quotation_symbol;
	}

	public static String quote(String str) {
		return quote(str, defaultQuotationSymbol);
	}
	
	public static String quote(char ch) {
		return quote(Character.toString(ch));
	}
	
	public static String quote(int i) {
		return quote(Integer.toString(i));
	}

	public static String refToValue(ClassFile classFile, String str) {
		if (str.indexOf("#") == -1) {
			return str;
		} else if (str.startsWith("\"") && str.endsWith("\"")) {
			return str;
		}
		Pattern p = Pattern.compile("#\\d+");
		Matcher m = p.matcher(str);
		if (m.find()){
			int cpIndex = Integer.parseInt(str.substring(m.start() + 1, m.end()));
			String cpValue = classFile.constantAt(cpIndex - 1).getValue();
			str = refToValue(classFile, (new StringBuffer(str)).replace(m.start(), m.end(), cpValue).toString());
		}
		return str;
	}
	
	public static String getClassName(ClassFile classFile) {
		String className = classFile.getClassName();
		if(className != null) { 
			return className;
		}
		try { 
			ConstantClass cc = ((ConstantClass)classFile.constantAt(classFile.getThisClass() - 1));
			if(cc == null) return null;
			short classNameIndex = cc.getNameIndex();
			ConstantUtf8 cu = (ConstantUtf8) classFile.constantAt(classNameIndex - 1);
			if(cu == null) return null;
			byte [] name = cu.getBytes();
			if(name == null) return null;
			return new String(name);
		}
		catch(ArrayIndexOutOfBoundsException e1) {
		}
		catch(ClassCastException e2) {
		}
		return null;
	}

	public static int bytesToInt(byte b1, byte b2, byte b3, byte b4) {
		return (b1 << 24 & 0xFFFFFFFF) |
		       (b2 << 16 & 0xFFFFFF) |
			   (b3 << 8 & 0xFFFF)|
			   (b4 & 0xFF) & 0xFFFFFFFF;
	}

	public static int bytesToInt(byte[] bytes, int offset) {
		return bytesToInt(bytes[offset], bytes[offset + 1],
						  bytes[offset + 2], bytes[offset + 3]);
	}

	public static short bytesToShort(byte b1, byte b2) {
		return (short) ((b1 << 8) | b2);
	}
	
	public static short bytesToShort(byte[] bytes, int offset) {
		return bytesToShort(bytes[offset], bytes[offset + 1]);
	}
	
	public static short bytesToUShort(byte b1, byte b2) {
		return (short) (((b1 << 8) | b2 & 0xFF) & 0xFFFF);
	}

	public static short bytesToUShort(byte[] bytes, int offset) {
		return bytesToUShort(bytes[offset], bytes[offset + 1]);
	}

	public static boolean isCPRef(String ref) {
		return Pattern.compile("#\\d+").matcher(ref).matches();
	}

	public static short cpRefValue(String str)
	throws NumberFormatException {
	    return (short) Integer.parseInt(str.substring(1, str.length()));
	}
}
