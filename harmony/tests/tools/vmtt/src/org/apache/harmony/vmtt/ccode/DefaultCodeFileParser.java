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
package org.apache.harmony.vmtt.ccode;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.harmony.vmtt.ErrorMessages;
import org.apache.harmony.vmtt.Utils;
import org.apache.harmony.vmtt.disasm.Instruction;
import org.apache.harmony.vmtt.disasm.InstructionsTable;
import org.apache.harmony.vmtt.ir.*;
import org.apache.harmony.vmtt.ir.attributes.*;
import org.apache.harmony.vmtt.ir.constants.*;
import org.apache.harmony.vmtt.ir.modifiers.*;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.17 $
 */

public class DefaultCodeFileParser
    extends CodeFileParser
    implements ErrorMessages {
	
	public static final char CC_QUOTATION		= '"';
	public static final char CC_EQUALS			= '=';
	public static final char CC_LEFT_CBRACKET	= '{';
	public static final char CC_RIGHT_CBRACKET	= '}';
	public static final char CC_LOW_LINE		= '_';
	public static final char CC_NUMBER			= '#';
	public static final char CC_COLON			= ':';

	public static final String KW_AUTO = "AUTO";
	
	private int token;
	
	public DefaultCodeFileParser() {
	    super();
	}
	
	public DefaultCodeFileParser(ClassFile cf, File inputFile)
	    throws NullPointerException, FileNotFoundException {
	    super(cf, inputFile);
	}

	public void parse()
	    throws CodeFileFormatException, NumberFormatException, IOException {
		setupSyntax();
		do {
			token = st.nextToken();
			if (isWord(token)) {
				parseKeywords(st.sval);
			}
		} while (token != StreamTokenizer.TT_EOF);
	}
	
	protected void parseKeywords(String str)
	    throws IOException, CodeFileFormatException, NumberFormatException {
		if(str.equals("class_name")) {
			classFile.setClassName(parseQuotation());
		}else if (str.equals("magic")) {
			classFile.setMagic(parseNumber().intValue());
		} else if (str.equals("minor_version")) {
			classFile.setMinorVersion(parseNumber().shortValue());
		} else if (str.equals("major_version")){
			classFile.setMajorVersion(parseNumber().shortValue());
		} else if (str.equals("constant_pool_count")){
		    Number num = parseNumberAuto();
		    if (num instanceof AutoValue) {
		        classFile.setAutodef(ClassFile.CONSTANT_POOL_COUNT);
		    } else {
		        classFile.setCPCount(num.shortValue());
		    }
		} else if (str.equals("constant_pool")){
			parseConstantPool();
		} else if (str.equals("access_flags")) {
			classFile.setAccessFlags(parseAccessFlags("access_flags",
                                                      ClassModifiers.classModifiers));
		} else if (str.equals("this_class")) {
			classFile.setThisClass(parseNumber().shortValue());
		} else if (str.equals("super_class")) {
			classFile.setSuperClass(parseNumber().shortValue());
		} else if (str.equals("interfaces_count")) {
		    Number num = parseNumberAuto();
		    if (num instanceof AutoValue) {
		        classFile.setAutodef(ClassFile.INTERFACES_COUNT);
		    } else {
		        classFile.setInterfacesCount(num.shortValue());
		    }
		} else if (str.equals("interfaces")) {
			parseInterfaces();
		} else if (str.equals("fields_count")) {
		    Number num = parseNumberAuto();
		    if (num instanceof AutoValue) {
		        classFile.setAutodef(ClassFile.FIELDS_COUNT);
		    } else {
				classFile.setFieldsCount(num.shortValue());
		    }
		} else if (str.equals("fields")) {
			parseFields();
		} else if (str.equals("methods_count")) {
		    Number num = parseNumberAuto();
		    if (num instanceof AutoValue) {
		        classFile.setAutodef(ClassFile.METHODS_COUNT);
		    } else {
				classFile.setMethodsCount(num.shortValue());
		    }
		} else if (str.equals("methods")) {
			parseMethods();
		} else if (str.equals("attributes_count")) {
		    Number num = parseNumberAuto();
		    if (num instanceof AutoValue) {
		        classFile.setAutodef(ClassFile.ATTRIBUTES_COUNT);
		    } else {
				classFile.setAttributesCount(num.shortValue());
		    }
		} else if (str.equals("attributes")) {
			parseAttributes(classFile);
		} else {
		    String msg = Utils.quote(str) + errs[E_ILL_KWRD];
		    throw new CodeFileFormatException(st.lineno(), msg);
		}
	}
	
	protected String parseQuotation()
	throws IOException, CodeFileFormatException {

		if (isNextToken(CC_EQUALS)) {
			token = st.nextToken();
			if (token == CC_QUOTATION) {
				return st.sval;
			} else {
				throw new CodeFileFormatException(st.lineno(), errs[E_QUOT_EXP]);
			}
		} else {
		    String msg = Utils.quote(CC_EQUALS) + errs[E_EXP];
			throw new CodeFileFormatException(st.lineno(), msg);
		}
	}
	
	protected Number parseNumber()
	    throws IOException, CodeFileFormatException, NumberFormatException {
		return parseNumber(true, false);
	}
	
	protected Number parseNumberAuto()
	    throws IOException, CodeFileFormatException, NumberFormatException {
		return parseNumber(true, true);
	}

	protected Number parseNumber(boolean equals, boolean auto)
	    throws IOException, CodeFileFormatException, NumberFormatException {
		if (equals) {
			if (!isNextToken(CC_EQUALS)) {
			    String msg = Utils.quote(CC_EQUALS) + errs[E_EXP];
				throw new CodeFileFormatException(st.lineno(), msg);
			}
		}
		token = st.nextToken();
		if (isNumber(token)) {
			return new Double(st.nval);
		} else if (isWord(token)) {
			String str = st.sval;
			if (Utils.isCPRef(str)) {
				return new Short(Utils.cpRefValue(str));
			} else if (Utils.isHex(str)) {
				return new Integer(Utils.hexToDec(str));
			} else if (auto && str.equals(KW_AUTO)) {
			    return new AutoValue();
			} else {
			    String msg = Utils.quote(str) + errs[E_NOT_HEX_CPR];
				throw new NumberFormatException(msg);
			}
		} else {
			throw new CodeFileFormatException(st.lineno(), errs[E_DEC_HEX_CPR_EXP]);
		}
	}
	
	protected byte[] parseAssembler()
	throws IOException, CodeFileFormatException {
		
		if (isNextTokenLeftBracket()) {
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    DataOutputStream dos = new DataOutputStream(baos);
			while (isNextTokenNotRightBracket()) {
			    if (isEOL(token)) {
			    } else if (isNumber(token)) {
			        token = st.nextToken();
			        if (token == CC_COLON) {
			            token = st.nextToken();
			            if (isWord(token)) {
					        String str = st.sval;
				            Instruction instr = InstructionsTable.getInstruction(str);
				            if (instr != null) {
				                instr.parse(st, dos);
				            } else {
				                throw new CodeFileFormatException(st.lineno(),
				                                                  Utils.quote(str) +
				                                                  errs[E_ILL_VMI]);
				            }
			            }
			        } else {
			            throw new CodeFileFormatException(st.lineno(),
			                							  Utils.quote(CC_COLON) +
			                							  errs[E_EXP_AFTER] +
			                							  "label");
			        }
			    } else {
			        throw new CodeFileFormatException(st.lineno(),
			                                          errs[E_LBL_EXP]);
			    }
			}
			return baos.toByteArray();
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("code asm"));
		}
	}
	
	protected byte[] parseByteSet()
	throws IOException, CodeFileFormatException, NumberFormatException {
		
		if (isNextTokenLeftBracket()) {
			ArrayList vec = new ArrayList();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if(isWord(token)) {
					String str = st.sval;
					if (Utils.isHex(str)) {
						int dec = Utils.hexToDec(str);
						vec.add(new Integer(dec));
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_NOT_HEX]);
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_HEX_EXP]);
				}
			}
			byte[] bytes = new byte[vec.size()];
			for (int i = 0; i < vec.size(); i++) {
				bytes[i] = ((Integer) vec.get(i)).byteValue();
			}
			return bytes;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP]);
		}
	}

	protected void parseConstantPool()
	throws IOException, CodeFileFormatException {

		if (isNextTokenLeftBracket()) {
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else 	if (isWord(token)) {
					String str = st.sval;
					if (str.equals(CPTags.getConstantType(CPTags.CONSTANT_Class))) {
						classFile.addConstant(parseConstant_Class());
					} else if (str.equals(CPTags.getConstantType(CPTags.CONSTANT_Fieldref)) ||
					           str.equals(CPTags.getConstantType(CPTags.CONSTANT_Methodref)) ||
							   str.equals(CPTags.getConstantType(CPTags.CONSTANT_InterfaceMethodref))) {
						classFile.addConstant(parseConstant_Ref(CPTags.getConstantTag(str)));
					} else if (str.equals(CPTags.getConstantType(CPTags.CONSTANT_String))) {
						classFile.addConstant(parseConstant_String());
					} else if (str.equals(CPTags.getConstantType(CPTags.CONSTANT_Integer)) ||
					           str.equals(CPTags.getConstantType(CPTags.CONSTANT_Float))) {
						classFile.addConstant(parseConstant_Num32(CPTags.getConstantTag(str)));
					} else if (str.equals(CPTags.getConstantType(CPTags.CONSTANT_Double)) ||
					           str.equals(CPTags.getConstantType(CPTags.CONSTANT_Long))) {
						classFile.addConstant(parseConstant_Num64(CPTags.getConstantTag(str)));
					} else if (str.equals(CPTags.getConstantType(CPTags.CONSTANT_NameAndType))) {
						classFile.addConstant(parseConstant_NameAndType());
					} else if (str.equals(CPTags.getConstantType(CPTags.CONSTANT_Utf8))) {
						classFile.addConstant(parseConstant_UTF8());
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_ILL_TP_CON]);
					}
				} else if (token == CC_LEFT_CBRACKET) {
					st.pushBack();
					classFile.addConstant(parseConstant_General());
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_CONST_EXP]);
				}
			}
			if (classFile.isAutodef(ClassFile.CONSTANT_POOL_COUNT)) {
			    classFile.setCPCount((short) (classFile.getActualCPCount() + 1));
			}
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("constant_pool"));
		}
	}
	
	protected ConstantGeneral parseConstant_General()
	throws IOException, CodeFileFormatException, NumberFormatException {
		
		ConstantGeneral c = new ConstantGeneral(parseByteSet());
		return c;
	}
	
	protected ConstantClass parseConstant_Class()
	throws IOException, CodeFileFormatException, NumberFormatException {
		
		ConstantClass c = new ConstantClass();
		c.setNameIndex(parseNumber().shortValue());
		return c;
	}

	protected ConstantRef parseConstant_Ref(byte tag)
	throws IOException, CodeFileFormatException, NumberFormatException {
	
		ConstantRef c = new ConstantRef(tag);
		c.setClassIndex(parseNumber().shortValue());
		c.setNameAndTypeIndex(parseNumber(false, false).shortValue());
		return c;
	}

	protected ConstantString parseConstant_String()
	throws IOException, CodeFileFormatException, NumberFormatException {
	
		ConstantString c = new ConstantString();
		c.setStringIndex(parseNumber().shortValue());
		return c;
	}

	protected ConstantNum32 parseConstant_Num32(byte tag)
	throws IOException, CodeFileFormatException, NumberFormatException {
		if(tag==CPTags.CONSTANT_Float) {
			ConstantNum32 c = new ConstantNum32(tag);
			c.setBytes(Float.floatToIntBits(parseNumber().floatValue()));
			return c;
		}
		ConstantNum32 c = new ConstantNum32(tag);
		c.setBytes(parseNumber().intValue());
		return c;
	}
	
	protected ConstantNum64 parseConstant_Num64(byte tag)
	throws IOException, CodeFileFormatException, NumberFormatException {

		ConstantNum64 c = new ConstantNum64(tag);
		long l = 0L;
		if(tag==CPTags.CONSTANT_Long) {
			l = parseNumber().longValue();
		} else {
			l = Double.doubleToLongBits(parseNumber().doubleValue());
		}
		c.setLowBytes((int) l);
		c.setHighBytes((int) (l >> 32));
		return c;
	}

	protected ConstantNameAndType parseConstant_NameAndType()
	throws IOException, CodeFileFormatException, NumberFormatException {

		ConstantNameAndType c = new ConstantNameAndType();
		c.setNameIndex(parseNumber().shortValue());
		c.setDescriptorIndex(parseNumber(false, false).shortValue());
		return c;
	}

	protected ConstantUtf8 parseConstant_UTF8()
	throws IOException, CodeFileFormatException {

		ConstantUtf8 c = new ConstantUtf8();
		String str = parseQuotation();
		c.setLength((short) str.length());
		c.setBytes(str.getBytes());
		return c;
	}
	
	protected short parseAccessFlags(String strAF, Modifiers modifiers)
	    throws IOException, CodeFileFormatException {
		short access_flags = 0;
		if (isNextToken(CC_EQUALS)) {
			while ((token = st.nextToken()) != StreamTokenizer.TT_EOL) {
				if (isWord(token)) {
					String str = st.sval;
                    if (Utils.isHex(str)) {
                        access_flags |= Utils.hexToDec(str);
                    } else if (ModifiersWrapper.isModifier(str, modifiers)) {
                        access_flags |= ModifiersWrapper.getModifiers(str, modifiers);
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  errs[E_ILL_MOD] +
						                                  Utils.quote(str));
					}
				} else if (isNumber(token)) {
					access_flags |= (int) st.nval;
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_MOD_HEX_EXP]);
				}
			}
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_EQUALS) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote(strAF));
		}
		return access_flags;
	}
	
	protected void parseInterfaces()
	throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isNumber(token)) {
					Double d = new Double(st.nval);
					classFile.addInterface(d.shortValue());
				} else if (isWord(token)) {
					String str = st.sval;
					if (Utils.isCPRef(str)) {
						classFile.addInterface(new Short(Utils.cpRefValue(str)).shortValue());
					} else if (Utils.isHex(str)) {
						Integer dec = new Integer(Utils.hexToDec(str));
						classFile.addInterface(new Short(dec.shortValue()).shortValue());
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_NOT_HEX_CPR]);
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_DEC_HEX_CPR_EXP]);
				}
			}
			if (classFile.isAutodef(ClassFile.INTERFACES_COUNT)) {
			    classFile.setInterfacesCount((short) classFile.getActualInterfacesCount());
			}
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("interfaces"));
		}
	}
	
	protected void parseFields()
	throws IOException, CodeFileFormatException {
		if (isNextTokenLeftBracket()) {
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("field")) {
						classFile.addField(parseField());
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  errs[E_FLD_EXP]);
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_FLD_EXP]);
				}
			}
			if (classFile.isAutodef(ClassFile.FIELDS_COUNT)) {
			    classFile.setFieldsCount((short) classFile.getActualFieldsCount());
			}
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("fields"));
		}
	}
	
	protected Field parseField()
	    throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			Field field = new Field();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("access_flag")) {
						field.setAccessFlag(parseAccessFlags("access_flag", 
                                                             FieldModifiers.fieldModifiers));
					} else if (str.equals("name_index")) {
						field.setNameIndex(parseNumber().shortValue());
					} else if (str.equals("descriptor_index")) {
						field.setDescriptorIndex(parseNumber().shortValue());
					} else if (str.equals("attributes_count")) {
					    Number num = parseNumberAuto();
					    if (num instanceof AutoValue) {
					        field.setAutodef(Field.ATTRIBUTES_COUNT);
					    } else {
					        field.setAttributesCount(num.shortValue());
					    }
					} else if (str.equals("attributes")) {
						parseAttributes(field);
						if (field.isAutodef(Field.ATTRIBUTES_COUNT)) {
						    field.setAttributesCount((short) field.getActualAttributesCount());
						}
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_ILL_PAR_FLD]);
					}
				} else {
					throw new CodeFileFormatException(st.lineno(), errs[E_PAR_FLD_EXP]);
				}
			}
			return field;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("field"));
		}
	}

	protected void parseMethods()
	throws IOException, CodeFileFormatException {
		if (isNextTokenLeftBracket()) {
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("method")) {
						classFile.addMethod(parseMethod());
					} else {
						throw new CodeFileFormatException(st.lineno(), errs[E_MTD_EXP]);
					}
				} else {
					throw new CodeFileFormatException(st.lineno(), errs[E_MTD_EXP]);
				}
			}
			if (classFile.isAutodef(ClassFile.METHODS_COUNT)) {
			    classFile.setMethodsCount((short) classFile.getActualMethodsCount());
			}
		} else {
			throw new CodeFileFormatException(st.lineno(), 
			                                  Utils.quote(CC_LEFT_CBRACKET) + 
			                                  errs[E_EXP_AFTER] + 
			                                  Utils.quote("methods"));
		}
	}

	protected Method parseMethod()
	    throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			Method method = new Method();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("access_flag")) {
						method.setAccessFlag(parseAccessFlags("access_flag", 
                                                              MethodModifiers.methodModifiers));
					} else if (str.equals("name_index")) {
						method.setNameIndex(parseNumber().shortValue());
					} else if (str.equals("descriptor_index")) {
						method.setDescriptorIndex(parseNumber().shortValue());
					} else if (str.equals("attributes_count")) {
					    Number num = parseNumberAuto();
					    if (num instanceof AutoValue) {
					        method.setAutodef(Method.ATTRIBUTES_COUNT);
					    } else {
					        method.setAttributesCount(num.shortValue());
					    }
					} else if (str.equals("attributes")) {
						parseAttributes(method);
						if (method.isAutodef(Method.ATTRIBUTES_COUNT)) {
						    method.setAttributesCount((short) method.getActualAttributesCount());
						}
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) + 
						                                  errs[E_ILL_PAR_MTD]);
					}
				} else {
					throw new CodeFileFormatException(st.lineno(), 
					                                  errs[E_PAR_MTD_EXP]);
				}
			}
			return method;
		} else {
			throw new CodeFileFormatException(st.lineno(), 
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("method"));
		}
	}
	
	protected void parseAttributes(Attributes attribs)
	    throws IOException, CodeFileFormatException {
	    ArrayList attributes = attribs.getAttributes();
	    if (isNextTokenLeftBracket()) {
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute")) {
						attributes.add(parseAttribute());
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  errs[E_ATR_EXP]);
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_ATR_EXP]);
				}
			}
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) + 
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attributes"));
		}
	}

	protected Attribute parseAttribute()
	    throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			st.pushBack();
			return parseAttrGeneral();
		} else if (isWord(token)){
			String str = st.sval;
			if (str.equals("ConstantValue")) {
				return parseAttrConstantValue();
			} else if (str.equals("Code")) {
				return parseAttrCode();
			} else if (str.equals("Exceptions")) {
				return parseAttrExceptions();
			} else if (str.equals("InnerClasses")) {
				return parseAttrInnerClasses();
			} else if (str.equals("Synthetic")) {
				return parseAttrSynthetic();
			} else if (str.equals("SourceFile")) {
				return parseAttrSourceFile();
			} else if (str.equals("LineNumberTable")) {
				return parseAttrLineNumberTable();
			} else if (str.equals("LocalVariableTable")) {
				return parseAttrLocalVariableTable(); 
			} else if (str.equals("LocalVariableTypeTable")) {
				return parseAttrLocalVariableTypeTable(); 
			} else if (str.equals("Deprecated")) { 
				return parseAttrDeprecated();
			} else if (str.equals("SourceDebugExtension")) { 
				return parseAttrSourceDebugExtension();
            } else if (str.equals("Signature")) {
                return parseAttrSignature();
            } else if (str.equals("EnclosingMethod")) {
                return parseAttrEnclosingMethod();
            } else if (str.equals("AnnotationDefault")) {
                return parseAttrAnnotationDefault();
            } else if (str.equals("RuntimeVisibleAnnotations")) {
                return parseAttrRuntimeVisibleAnnotations();
            } else if (str.equals("RuntimeVisibleParameterAnnotations")) {
                return parseAttrRuntimeVisibleParameterAnnotations();
            } else if (str.equals("RuntimeInvisibleAnnotations")) {
                return parseAttrRuntimeInvisibleAnnotations();
            } else if (str.equals("RuntimeInvisibleParameterAnnotations")) {
                return parseAttrRuntimeInvisibleParameterAnnotations();
			} else {
				throw new CodeFileFormatException(st.lineno(),
				                                  Utils.quote(str) +
				                                  errs[E_ILL_TP_ATR]);
			}
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute <type>"));
		}
	}
	
	protected General parseAttrGeneral()
	    throws IOException, CodeFileFormatException, NumberFormatException {
		General attr = new General();
		attr.setBytes(parseByteSet());
		return attr;
	}
	
	protected ElementValue parseElementValue()
	throws IOException, CodeFileFormatException, NumberFormatException {
		String str = st.sval;
		if(!"tag".equals(str)) 
			throw new CodeFileFormatException(st.lineno(),
					Utils.quote("tag") +
					errs[E_EXP]);
			
		ElementValue elementValue = new ElementValue();
		
		if(!isNextToken(CC_EQUALS))
			throw new CodeFileFormatException(st.lineno(), "tag value" 
					+ errs[E_EXP]);
		
		token= st.nextToken();
			
		if (isWord(token) || token == '@' || token == '[') {
			byte tag;
			String s;
			if(isWord(token)) {
				s = st.sval;
				if(s.length() > 1) 
					throw new CodeFileFormatException(st.lineno(), "tag should be one character length");
				tag = (byte) s.charAt(0);
			}
			else {
				s ="" +  ((char) token);
				tag = (byte) token; 
			}
			
			elementValue.setTag(tag);
			ElementValue.value val = null;
			skipLF();
			switch(tag) {
			case 'B': 
			case 'C': 
			case 'D': 
			case 'F': 
			case 'I': 
			case 'J': 
			case 'S': 
			case 'Z':
			case 's':
				if (isWord(token)) {
                    String s0 = st.sval;
                    if (s0.equals("const_value_index")) {
                    	val = new ElementValue.const_value(parseNumber().shortValue());
                    }
                    else throw new CodeFileFormatException(st.lineno(), 
                    		Utils.quote("const_value") + errs[E_EXP] + " instead of " +
							Utils.quote(s0) +" for tag " + s);
				}
				else throw new CodeFileFormatException(st.lineno(), 
						Utils.quote("const_value") + errs[E_EXP] + 
						" for tag " + s);
				break;
			case 'e':
				if (isWord(token)) {
                    String s0 = st.sval;
                    if (s0.equals("type_name_index")) {
                    	short type_name_index = parseNumber().shortValue();
                    	skipLF();
                    	if (isWord(token)) {
                    		String s1 = st.sval;
                    		if (s1.equals("const_name_index")) {
                    			short const_name_index = parseNumber().shortValue();
                    			val = new ElementValue.enum_value(type_name_index, const_name_index);
                    		}
                    		else throw new CodeFileFormatException(st.lineno(), 
                    				   Utils.quote("const_name_index") + errs[E_EXP] + " instead of " +
									   Utils.quote(s1) +" for tag " + s);
                    	}
                    }
                    else throw new CodeFileFormatException(st.lineno(), 
                    		Utils.quote("type_name_index") + errs[E_EXP]+ " instead of "+ 
							Utils.quote(s0) + " for tag " + s);
				}
				else
					throw new CodeFileFormatException(st.lineno(), 
							Utils.quote("type_name_index") + errs[E_EXP] + " for tag " + s);
				break;
				case 'c':
				if (isWord(token)) {
                    String s0 = st.sval;
                    if (s0.equals("class_info_index")) {
                    	val = new ElementValue.const_value(parseNumber().shortValue());
                    }
                    else throw new CodeFileFormatException(st.lineno(), 
                    		Utils.quote("class_info_index") + errs[E_EXP] + " instead of "+ 
							Utils.quote(s0) +" for tag " + s);
				}
				else throw new CodeFileFormatException(st.lineno(), 
						Utils.quote("class_info_index") + errs[E_EXP] + " for tag " + s);
				break;
			case '@':
				if (isWord(token)) {
					String s0 = st.sval;
					if (s0.equals("Annotation")) {
						Annotation annotation = parseAnnotation();
						val = new ElementValue.annotation_value(annotation);
					}
					else throw new CodeFileFormatException(st.lineno(), 
						Utils.quote("Annotation") + errs[E_EXP] + " instead of "+ 
						Utils.quote(s0) +" for tag " + s);
				}
				else throw new CodeFileFormatException(st.lineno(), 
						Utils.quote("Annotation") + errs[E_EXP] + " for tag " + s);
				break;
			case '[':
				if (isWord(token)) {
					String s0 = st.sval;
                    if (s0.equals("array_length")) {
                    	int num = parseNumber().shortValue();
                    	if(isNextTokenLeftBracket()) {
                    		ArrayList array_values = new ArrayList();  
                    		while (isNextTokenNotRightBracket()) {
                    			if (isEOL(token)) {
                    				continue;
                    			}
                				array_values.add(parseElementValue());
                    		}
                    		ElementValue [] array_val = (ElementValue[]) array_values.toArray(new ElementValue[0]);
                    		val = new ElementValue.array_value(array_val, num);
                    	}
                    	else {
                    		st.pushBack();
                    		ElementValue [] array_val = new ElementValue[num];
                    		for(int i = 0; i < num; ++i) {
                   				skipLF();
                    			array_val[i] = parseElementValue();
                    		}
                    		val = new ElementValue.array_value(array_val);
                    	}
						
                    }
                    else throw new CodeFileFormatException(st.lineno(), 
                    		Utils.quote("array_length") + errs[E_EXP] + " instead of "+ 
							Utils.quote(s0) +" for tag " + s);
					
				}
				else throw new CodeFileFormatException(st.lineno(), 
						Utils.quote("array_length") + errs[E_EXP] +" for tag " + s);
				break;
			default: throw new CodeFileFormatException(st.lineno(), 
					"Unexpected tag value " + Utils.quote(s));
			}
			elementValue.setValue(val);
			return elementValue;
		}
		throw new CodeFileFormatException(st.lineno(), "tag value" + errs[E_EXP] );
	}
	
	protected AnnotationDefault parseAttrAnnotationDefault()
	throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			AnnotationDefault attr = new AnnotationDefault();
			while (isNextTokenNotRightBracket()) {
                if (isEOL(token)) {
                } else if (isWord(token)) {
                    String str = st.sval;
                    if (str.equals("attribute_name_index")) {
                        attr.setAttributeNameIndex(parseNumber().shortValue());
                    } else if (str.equals("attribute_length")) {
                        attr.setAttributeLength(parseNumber().intValue());
                    } else {
                    	ElementValue elementValue = parseElementValue();
                    	attr.setElementValue(elementValue);
                    }
                }
			}
			return attr;
		}
		else {
			throw new CodeFileFormatException(st.lineno(),
                Utils.quote(CC_LEFT_CBRACKET) +
                errs[E_EXP_AFTER] +
                Utils.quote("attribute AnnotationDefault"));
		}
		
	}
	
	protected RuntimeVisibleAnnotations parseAttrRuntimeVisibleAnnotations()
	throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			RuntimeVisibleAnnotations attr = new RuntimeVisibleAnnotations();
			do {
				token = st.nextToken();
                if (isEOL(token)) {
                } else if(token == StreamTokenizer.TT_EOF) {
                	throw new CodeFileFormatException(st.lineno(), "Unexpected end of file while parsing RuntimeVisibleAnnotations attribute." );
				}else if (isWord(token)) {
                    String str = st.sval;
                    if (str.equals("attribute_name_index")) {
                        attr.setAttributeNameIndex(parseNumber().shortValue());
                    } else if (str.equals("attribute_length")) {
                        attr.setAttributeLength(parseNumber().intValue());
                    } else if (str.equals("num_annotations")) {
                        attr.setNumAnnotations(parseNumber().shortValue());
                    } else {
                    	Annotation [] annotations = parseAnnotations();
                    	if(annotations == null) {
                    		annotations = new Annotation [0]; 
                    	}
                    	attr.setAnnotations(annotations);
                    }
                }
			} while (token != CC_RIGHT_CBRACKET);
			return attr;
		}
		else {
			throw new CodeFileFormatException(st.lineno(),
                Utils.quote(CC_LEFT_CBRACKET) +
                errs[E_EXP_AFTER] +
                Utils.quote("attribute RuntimeVisibleAnnotations"));
		}
		
	}
	
	protected RuntimeVisibleParameterAnnotations parseAttrRuntimeVisibleParameterAnnotations()
	throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			RuntimeVisibleParameterAnnotations attr = new RuntimeVisibleParameterAnnotations();
			ArrayList rvas = new ArrayList();
			while (isNextTokenNotRightBracket()) {
                if (isEOL(token)) {
                } else if (isWord(token)) {
                    String str = st.sval;
                    if (str.equals("attribute_name_index")) {
                        attr.setAttributeNameIndex(parseNumber().shortValue());
                    } else if (str.equals("attribute_length")) {
                        attr.setAttributeLength(parseNumber().intValue());
                    } else if (str.equals("num_parameters")) {
                        attr.setNumParameters(parseNumber().byteValue());
                    } else {
                    	throw new CodeFileFormatException(st.lineno(),
                    			Utils.quote(str) +
                                errs[E_ILL_PAR_ATR] +
                                Utils.quote("RuntimeVisibleParameterAnnotations"));
                    }
                } else if(token == CC_LEFT_CBRACKET) {
                	RuntimeVisibleAnnotations rva = new RuntimeVisibleAnnotations();
            		Annotation [] annotations = new Annotation [0];
                	do {
                		token = st.nextToken();
                        if (isEOL(token)) {
                        } else if(token == StreamTokenizer.TT_EOF) {
                        	throw new CodeFileFormatException(st.lineno(), "Unexpected end of file." );
                        } else if (isWord(token)) {
                            String str = st.sval;
                            if (str.equals("num_annotations")) {
                            	short num_annotations = parseNumber().shortValue();
                            	rva.setNumAnnotations(num_annotations);
                            	annotations = parseAnnotations();
                            }
                            else {
                            	throw new CodeFileFormatException(st.lineno(),
                                        Utils.quote("num_annotations") +
										errs[E_EXP]);
                            }
                        }
                    } 
                	while (token != CC_RIGHT_CBRACKET);
                	
                	if(annotations == null) {
                		annotations = new Annotation [0]; 
                	}
                	rva.setAnnotations(annotations);
                	rvas.add(rva);
                }
			}
			attr.setAnnotations( (RuntimeVisibleAnnotations []) rvas.toArray(new RuntimeVisibleAnnotations [0]));
			return attr;
		}
		else {
			throw new CodeFileFormatException(st.lineno(),
                Utils.quote(CC_LEFT_CBRACKET) +
                errs[E_EXP_AFTER] +
                Utils.quote("attribute RuntimeVisibleParameterAnnotations"));
		}
		
	}
	
	protected RuntimeInvisibleAnnotations parseAttrRuntimeInvisibleAnnotations()
	throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			RuntimeInvisibleAnnotations attr = new RuntimeInvisibleAnnotations();
			do {
				token = st.nextToken();
                if (isEOL(token)) {
                } else if(token == StreamTokenizer.TT_EOF) {
                	throw new CodeFileFormatException(st.lineno(), "Unexpected end of file while parsing RuntimeInvisibleAnnotations attribute." );
                } else if (isWord(token)) {
                    String str = st.sval;
                    if (str.equals("attribute_name_index")) {
                        attr.setAttributeNameIndex(parseNumber().shortValue());
                    } else if (str.equals("attribute_length")) {
                        attr.setAttributeLength(parseNumber().intValue());
                    } else if (str.equals("num_annotations")) {
                        attr.setNumAnnotations(parseNumber().shortValue());
                    } else {
                    	Annotation [] annotations = parseAnnotations();
                    	if(annotations == null) {
                    		annotations = new Annotation [0]; 
                    	}
                    	attr.setAnnotations(annotations);
                    }
                }
			} while (token != CC_RIGHT_CBRACKET);
			return attr;
		}
		else {
			throw new CodeFileFormatException(st.lineno(),
                Utils.quote(CC_LEFT_CBRACKET) +
                errs[E_EXP_AFTER] +
                Utils.quote("attribute RuntimeInvisibleAnnotations"));
		}
		
	}
	
	protected RuntimeInvisibleParameterAnnotations parseAttrRuntimeInvisibleParameterAnnotations()
	throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			RuntimeInvisibleParameterAnnotations attr = new RuntimeInvisibleParameterAnnotations();
			ArrayList rias = new ArrayList();
			while (isNextTokenNotRightBracket()) {
                if (isEOL(token)) {
                } else if (isWord(token)) {
                    String str = st.sval;
                    if (str.equals("attribute_name_index")) {
                        attr.setAttributeNameIndex(parseNumber().shortValue());
                    } else if (str.equals("attribute_length")) {
                        attr.setAttributeLength(parseNumber().intValue());
                    } else if (str.equals("num_parameters")) {
                        attr.setNumParameters(parseNumber().byteValue());
                    } else {
                    	throw new CodeFileFormatException(st.lineno(),
                    			Utils.quote(str) +
                                errs[E_ILL_PAR_ATR] +
                                Utils.quote("RuntimeInvisibleParameterAnnotations"));
                    }
                } else if(token == CC_LEFT_CBRACKET) {
                	RuntimeInvisibleAnnotations ria = new RuntimeInvisibleAnnotations();
            		Annotation [] annotations = new Annotation [0];
                	do {
                		token = st.nextToken();
                        if (isEOL(token)) {
                        } else if(token == StreamTokenizer.TT_EOF) {
                        	throw new CodeFileFormatException(st.lineno(), "Unexpected end of file." );
                        } else if (isWord(token)) {
                            String str = st.sval;
                            if (str.equals("num_annotations")) {
                            	short num_annotations = parseNumber().shortValue();
                            	ria.setNumAnnotations(num_annotations);
                            	annotations = parseAnnotations();
                            }
                            else {
                            	throw new CodeFileFormatException(st.lineno(),
                                        Utils.quote("num_annotations") +
										errs[E_EXP]);
                            }
                        }
                    } 
                	while (token != CC_RIGHT_CBRACKET);
                	
                	if(annotations == null) {
                		annotations = new Annotation [0]; 
                	}
                	ria.setAnnotations(annotations);
                	rias.add(ria);
                }
			}
			attr.setAnnotations( (RuntimeInvisibleAnnotations []) rias.toArray(new RuntimeInvisibleAnnotations [0]));
			return attr;
		}
		else {
			throw new CodeFileFormatException(st.lineno(),
                Utils.quote(CC_LEFT_CBRACKET) +
                errs[E_EXP_AFTER] +
                Utils.quote("attribute RuntimeInvisibleParameterAnnotations"));
		}
		
	}
	
	protected Annotation [] parseAnnotations()
	throws IOException, CodeFileFormatException, NumberFormatException {
		ArrayList annotations = new ArrayList();
		 do {
			if(isEOL(token)) {
			} else if(token == StreamTokenizer.TT_EOF) {
				throw new CodeFileFormatException(st.lineno(), "Unexpected end of file while parsing Annotations." );
			}
			else if (isWord(token)) {
				String str = st.sval;
				if (str.equals("Annotation")) {
					annotations.add(parseAnnotation());
				}
				else {
					throw new CodeFileFormatException(st.lineno(), "Unexpected " + Utils.quote(str));
				}
			}
			/*else if(token !=  CC_RIGHT_CBRACKET) {
				throw new CodeFileFormatException(st.lineno(),
						Utils.quote("Annotation") +
						errs[E_EXP]);*
			}*/
		} while (isNextTokenNotRightBracket());
		 return (Annotation []) annotations.toArray(new Annotation[0]);
		
	}
	
	protected Annotation  parseAnnotation()
	throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			Annotation annotation = new Annotation();
			ElementValuePair [] evps = new ElementValuePair[0];
			boolean wasTypeIndex = false;
			do {
			 	token = st.nextToken();
                if (isEOL(token)) {
                } else if(token == StreamTokenizer.TT_EOF) {
    				throw new CodeFileFormatException(st.lineno(), "Unexpected end of file while parsing Annotation." );
                } else if (isWord(token)) {
                    String str = st.sval;
                    if (str.equals("type_index")) {
                    	annotation.setTypeIndex(parseNumber().shortValue());
                    	wasTypeIndex = true;
                    } else if (str.equals("num_element_value_pairs")) {
                    	annotation.setNumElementValues(parseNumber().shortValue());
                    } else {
                    	throw new CodeFileFormatException(st.lineno(),
                    			Utils.quote(str) +
                                errs[E_ILL_PAR_ATR] +
                                Utils.quote("Annotation"));
                    }
                } else if (token == CC_LEFT_CBRACKET) {
                	if(!wasTypeIndex) {
                		throw new CodeFileFormatException(st.lineno(),
                                Utils.quote("type_index") +
								errs[E_EXP]);
                	}
                	evps = parseElementValuePairs();
                }
			} while (token!= CC_RIGHT_CBRACKET);
				annotation.setElementValuePairs(evps);
			return annotation;
		}
		else {
			throw new CodeFileFormatException(st.lineno(),
                Utils.quote(CC_LEFT_CBRACKET) +
                errs[E_EXP_AFTER] +
                Utils.quote("Annotation"));
		}
		
	}
	
	protected ElementValuePair [] parseElementValuePairs() 
		throws IOException, CodeFileFormatException, NumberFormatException {
		java.util.ArrayList elementValuePairs = new java.util.ArrayList();
		 do {
		 	if (!isEOL(token)) {
		 		elementValuePairs.add(parseElementValuePair());
		 	}
		} while (isNextTokenNotRightBracket());
		return (ElementValuePair []) elementValuePairs.toArray(new ElementValuePair[0]);
	}
	
	protected ElementValuePair parseElementValuePair() 
	throws IOException, CodeFileFormatException, NumberFormatException {
		if (token == CC_LEFT_CBRACKET) {
			ElementValuePair evp = new ElementValuePair();
			boolean wasElement_name_index = false;
			while (isNextTokenNotRightBracket()) {
                if (isEOL(token)) {
                } else if (isWord(token)) {
                    String str = st.sval;
                    if (str.equals("element_name_index")) {
                        evp.setElementNameIndex(parseNumber().shortValue());
                        wasElement_name_index = true;
                    } else if(wasElement_name_index) {
                    	evp.setElementValue(parseElementValue());
                    } else {
                    	throw new CodeFileFormatException(st.lineno(),
        						Utils.quote("element_name_index") +
        						errs[E_EXP]);
                    }
                } else {
                	throw new CodeFileFormatException(st.lineno(),
    						Utils.quote("element_name_index") +
    						errs[E_EXP]);
                }
			}
			return evp;
		}
		throw new CodeFileFormatException(st.lineno(),
				Utils.quote(CC_LEFT_CBRACKET) + 
				errs[E_EXP]);
		
	}
	
    protected EnclosingMethod parseAttrEnclosingMethod()
        throws IOException, CodeFileFormatException, NumberFormatException {
        if (isNextTokenLeftBracket()) {
            EnclosingMethod attr = new EnclosingMethod();
            while (isNextTokenNotRightBracket()) {
                if (isEOL(token)) {
                } else if (isWord(token)) {
                    String str = st.sval;
                    if (str.equals("attribute_name_index")) {
                        attr.setAttributeNameIndex(parseNumber().shortValue());
                    } else if (str.equals("attribute_length")) {
                        attr.setAttributeLength(parseNumber().intValue());
                    } else if (str.equals("class_index")) {
                        attr.setClassIndex(parseNumber().shortValue());
                    } else if (str.equals("method_index")) {
                        attr.setMethodIndex(parseNumber().shortValue());
                    } else {
                        throw new CodeFileFormatException(st.lineno(), Utils.quote(str) +
                                                          errs[E_ILL_PAR_ATR] +
                                                          Utils.quote("ConstantValue"));
                    }
                } else {
                    throw new CodeFileFormatException(st.lineno(), errs[E_PAR_ATR_EXP]);
                }
            }
            return attr;
        } else {
            throw new CodeFileFormatException(st.lineno(),
                                              Utils.quote(CC_LEFT_CBRACKET) +
                                              errs[E_EXP_AFTER] +
                                              Utils.quote("attribute ConstantValue"));
        }
    }
    
    protected Signature parseAttrSignature()
        throws IOException, CodeFileFormatException, NumberFormatException {
        if (isNextTokenLeftBracket()) {
            Signature attr = new Signature();
            while (isNextTokenNotRightBracket()) {
                if (isEOL(token)) {
                } else if (isWord(token)) {
                    String str = st.sval;
                    if (str.equals("attribute_name_index")) {
                        attr.setAttributeNameIndex(parseNumber().shortValue());
                    } else if (str.equals("attribute_length")) {
                        attr.setAttributeLength(parseNumber().intValue());
                    } else if (str.equals("signature_index")) {
                        attr.setSignatureIndex(parseNumber().shortValue());
                    } else {
                        throw new CodeFileFormatException(st.lineno(), Utils.quote(str) +
                                                          errs[E_ILL_PAR_ATR] +
                                                          Utils.quote("ConstantValue"));
                    }
                } else {
                    throw new CodeFileFormatException(st.lineno(), errs[E_PAR_ATR_EXP]);
                }
            }
            return attr;
        } else {
            throw new CodeFileFormatException(st.lineno(),
                                              Utils.quote(CC_LEFT_CBRACKET) +
                                              errs[E_EXP_AFTER] +
                                              Utils.quote("attribute ConstantValue"));
        }
    }
	
	protected ConstantValue parseAttrConstantValue()
	    throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			ConstantValue attr = new ConstantValue();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute_name_index")) {
						attr.setAttributeNameIndex(parseNumber().shortValue());
					} else if (str.equals("attribute_length")) {
						attr.setAttributeLength(parseNumber().intValue());
					} else if (str.equals("constantvalue_index")) {
						attr.setConstantValueIndex(parseNumber().shortValue());
					} else {
						throw new CodeFileFormatException(st.lineno(), Utils.quote(str) +
						                                  errs[E_ILL_PAR_ATR] +
						                                  Utils.quote("ConstantValue"));
					}
				} else {
					throw new CodeFileFormatException(st.lineno(), errs[E_PAR_ATR_EXP]);
				}
			}
			return attr;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute ConstantValue"));
		}
	}
	
	protected SourceFile parseAttrSourceFile()
	throws IOException, CodeFileFormatException, NumberFormatException {
	
		if (isNextTokenLeftBracket()) {
			SourceFile attr = new SourceFile();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute_name_index")) {
						attr.setAttributeNameIndex(parseNumber().shortValue());
					} else if (str.equals("attribute_length")) {
						attr.setAttributeLength(parseNumber().intValue());
					} else if (str.equals("sourcefile_index")) {
						attr.setSourceFileIndex(parseNumber().shortValue());
					} else {
						throw new CodeFileFormatException(st.lineno(), Utils.quote(str) +
						                                  errs[E_ILL_PAR_ATR] +
						                                  Utils.quote("SourceFile"));
					}
				} else {
					throw new CodeFileFormatException(st.lineno(), errs[E_PAR_ATR_EXP]);
				}
			}
			return attr;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute SourceFile"));
		}
	}

	protected Code parseAttrCode()
	    throws IOException, CodeFileFormatException, NumberFormatException {
		if (isNextTokenLeftBracket()) {
			Code attr = new Code();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute_name_index")) {
						attr.setAttributeNameIndex(parseNumber().shortValue());
					} else if (str.equals("attribute_length")) {
						attr.setAttributeLength(parseNumber().intValue());
					} else if (str.equals("max_stack")) {
						attr.setMaxStack(parseNumber().shortValue());
					} else if (str.equals("max_locals")) {
						attr.setMaxLocals(parseNumber().shortValue());
					} else if (str.equals("code_length")) {
						attr.setCodeLength(parseNumber().intValue());
					} else if (str.equals("code")) {
						parseAttrCodeCode(attr);
					} else if (str.equals("exception_table_length")) {
						attr.setExceptionTableLength(parseNumber().shortValue());
					} else if (str.equals("exception_table")) {
						parseAttrCodeException(attr);
					} else if (str.equals("attributes_count")) {
					    Number num = parseNumberAuto();
					    if (num instanceof AutoValue) {
					        attr.setAutodef(Code.ATTRIBUTES_COUNT);
					    } else {
					        attr.setAttributesCount(num.shortValue());
					    }
					} else if (str.equals("attributes")) {
						parseAttributes(attr);
						if (attr.isAutodef(Code.ATTRIBUTES_COUNT)) {
						    attr.setAttributesCount((short) attr.getActualAttributesCount());
						}
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_ILL_PAR_ATR] +
						                                  Utils.quote("Code"));
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_PAR_ATR_EXP]);
				}
			}
			return attr;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) + 
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute Code"));
		}
	}

	protected void parseAttrCodeCode(Code attr)
	throws IOException, CodeFileFormatException, NumberFormatException {
		
		token = st.nextToken();
		if (isWord(token)) {
			String str = st.sval;
			if (str.equalsIgnoreCase("asm")) {
				attr.setCode(parseAssembler());
			} else if (str.equalsIgnoreCase("bin")) {
				attr.setCode(parseByteSet());
			} else {
				throw new CodeFileFormatException(st.lineno(), Utils.quote(str) + 
				                                  errs[E_ILL_TP_COD]);
			}
		    //attr.setCodeLength(attr.getActualCodeLength());
		} else {
			throw new CodeFileFormatException(st.lineno(), errs[E_TP_COD_EXP]);
		}
	}


	protected void parseAttrCodeException(Code attr)
	throws IOException, CodeFileFormatException, NumberFormatException {
		
		if (isNextTokenLeftBracket()) {
			Vector vec = new Vector();
			while(isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (Utils.isHex(str)) {
						vec.add(new Integer(Utils.hexToDec(str)));
					} else {
						throw new CodeFileFormatException(st.lineno(), Utils.quote(str) +
						                                  errs[E_NOT_HEX]);
					}
				} else if (isNumber(token)) {
					vec.add(new Integer((int) st.nval));
				} else {
					throw new CodeFileFormatException(st.lineno(), errs[E_DEC_HEX_EXP]);
				}
			}
			
			Code.exception exc = attr.new exception();
			for (int i = 0; i < vec.size(); i++) {
				short val = ((Integer)vec.elementAt(i)).shortValue();
				if (!exc.isDefined(Code.exception.START_PC)) {
					exc.setStartPC(val);
				} else if (!exc.isDefined(Code.exception.END_PC)) {
					exc.setEndPC(val);
				} else if (!exc.isDefined(Code.exception.HANDLER_PC)) {
					exc.setHandlerPC(val);
				} else if (!exc.isDefined(Code.exception.CATCH_TYPE)) {
					exc.setCatchType(val);
				} else {
					attr.addException(exc);
					exc = attr.new exception();
					exc.setStartPC(val);
				}
			}
			attr.addException(exc);
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("exception_table"));
		}
	}
	
	protected LineNumberTable parseAttrLineNumberTable()
	throws IOException, CodeFileFormatException, NumberFormatException {
		
		if (isNextTokenLeftBracket()) {
			LineNumberTable attr = new LineNumberTable();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute_name_index")) {
						attr.setAttributeNameIndex(parseNumber().shortValue());
					} else if (str.equals("attribute_length")) {
						attr.setAttributeLength(parseNumber().intValue());
					} else if (str.equals("line_number_table_length")) {
						attr.setLineNumberTableLength(parseNumber().shortValue());
					} else if (str.equals("line_number_table")) {
						parseAttrLineNumberTableTable(attr);
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_ILL_PAR_ATR] +
						                                  Utils.quote("LineNumberTable"));
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_PAR_ATR_EXP]);
				}
			}
			return attr;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute LineNumberTable"));
		}
	}
	
	protected void parseAttrLineNumberTableTable(LineNumberTable attr)
	throws IOException, CodeFileFormatException, NumberFormatException {
		
		if (isNextTokenLeftBracket()) {
			Vector vec = new Vector();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (Utils.isHex(str)) {
						int dec = Utils.hexToDec(str);
						vec.add(new Integer(dec));
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_NOT_HEX]);
					}
				} else if (isNumber(token)) {
					vec.add(new Integer((int) st.nval));
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_DEC_HEX_EXP]);
				}
			}
			
			LineNumberTable.line_number ln = attr.new line_number();
			for (int i = 0; i < vec.size(); i++) {
				short val = ((Integer) vec.elementAt(i)).shortValue();
				if (!ln.isDefined(LineNumberTable.line_number.START_PC)) {
					ln.setStartPC(val);
				} else if (!ln.isDefined(LineNumberTable.line_number.LINE_NUMBER)) {
					ln.setLineNumber(val);
				} else {
					attr.addLineNumber(ln);
					ln = attr.new line_number();
					ln.setStartPC(val);
				}
			}
			attr.addLineNumber(ln);
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("line_number_table"));
		}
	}	
	
	protected Exceptions parseAttrExceptions()
	throws IOException, CodeFileFormatException, NumberFormatException {
		
		if (isNextTokenLeftBracket()) {
			Exceptions attr = new Exceptions();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute_name_index")) {
						attr.setAttributeNameIndex(parseNumber().shortValue());
					} else if (str.equals("attribute_length")) {
						attr.setAttributeLength(parseNumber().intValue());
					} else if (str.equals("number_of_exceptions")) {
						attr.setNumberOfException(parseNumber().shortValue());
					} else if (str.equals("exception_index_table")){
						parseAttrExceptionsTable(attr);
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_ILL_PAR_ATR] +
						                                  Utils.quote("Exceptions"));
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_PAR_ATR_EXP]);
				}
			}
			return attr;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute Exceptions"));
		}
	}
	
	protected void parseAttrExceptionsTable(Exceptions attr)
	throws IOException, CodeFileFormatException, NumberFormatException {
		
		if (isNextTokenLeftBracket()) {
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (Utils.isCPRef(str)) {
						attr.addException(Utils.cpRefValue(str));
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) + 
						                                  errs[E_NOT_CPR]);
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_CPR_EXP]);
				}
			}
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("exception_index_table"));
		}
	}
	
	protected Synthetic parseAttrSynthetic()
	throws IOException, CodeFileFormatException, NumberFormatException {
	
		if (isNextTokenLeftBracket()) {
			Synthetic attr = new Synthetic();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute_name_index")) {
						attr.setAttributeNameIndex(parseNumber().shortValue());
					} else if (str.equals("attribute_length")) {
						attr.setAttributeLength(parseNumber().intValue());
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_ILL_PAR_ATR] +
						                                  Utils.quote("Synthetic"));
					}
				} else {
					throw new CodeFileFormatException(st.lineno(), errs[E_PAR_ATR_EXP]);
				}
			}	
			return attr;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute Synthetic"));
		}
	}
	
	protected DeprecatedAttr parseAttrDeprecated()
	throws IOException, CodeFileFormatException, NumberFormatException {

		if (isNextTokenLeftBracket()) {
			DeprecatedAttr attr = new DeprecatedAttr();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute_name_index")) {
						attr.setAttributeNameIndex(parseNumber().shortValue());
					} else if (str.equals("attribute_length")) {
						attr.setAttributeLength(parseNumber().intValue());
					} else {
						throw new CodeFileFormatException(st.lineno(),
                                                          Utils.quote(str) +
						                                  errs[E_ILL_PAR_ATR] +
						                                  Utils.quote("Deprecated"));
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_PAR_ATR_EXP]);
				}
			}	
			return attr;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute Deprecated"));
		}
	}
	
	
	protected SourceDebugExtension parseAttrSourceDebugExtension()
	throws IOException, CodeFileFormatException, NumberFormatException {

		if (isNextTokenLeftBracket()) {
			SourceDebugExtension attr = new SourceDebugExtension();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute_name_index")) {
						attr.setAttributeNameIndex(parseNumber().shortValue());
					} else if (str.equals("attribute_length")) {
						attr.setAttributeLength(parseNumber().intValue());
					} else if (str.equals("debug_extension")) {
						attr.setDebugExtension(parseByteSet());
					} else {
						throw new CodeFileFormatException(st.lineno(),
                                                          Utils.quote(str) +
						                                  errs[E_ILL_PAR_ATR] +
						                                  Utils.quote("SourceDebugExtension"));
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_PAR_ATR_EXP]);
				}
			}	
			return attr;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute SourceDebugExtension"));
		}
	}
	protected LocalVariableTable parseAttrLocalVariableTable()
	throws IOException, CodeFileFormatException, NumberFormatException {
	
		if (isNextTokenLeftBracket()) {
			LocalVariableTable attr = new LocalVariableTable();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute_name_index")) {
						attr.setAttributeNameIndex(parseNumber().shortValue());
					} else if (str.equals("attribute_length")) {
						attr.setAttributeLength(parseNumber().intValue());
					} else if (str.equals("local_variable_table_length")) {
						attr.setLocalVariableTableLength(parseNumber().shortValue());
					} else if (str.equals("local_variable_table")) {
						parseAttrLocalVariableTableContent(attr);
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) + 
						                                  errs[E_ILL_PAR_ATR] +
						                                  Utils.quote("LocalVariableTable"));
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_PAR_ATR_EXP]);
				}
			}	
			return attr;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute LocalVariableTable"));
		}
	}
	
	protected void parseAttrLocalVariableTableContent(LocalVariableTable attr)
	throws IOException, CodeFileFormatException, NumberFormatException {

		if (isNextTokenLeftBracket()) {
			ArrayList vec = new ArrayList();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (Utils.isHex(str)) {
						int dec = Utils.hexToDec(str);
						vec.add(new Integer(dec));
					} else if (Utils.isCPRef(str)) {
						short ref = Utils.cpRefValue(str);
						vec.add(new Integer((int)ref));
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_NOT_HEX_CPR]);
					}
				} else if (isNumber(token)) {
					vec.add(new Integer((int) st.nval));
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_DEC_HEX_CPR_EXP]);
				}
			}
			
			LocalVariableTable.local_variable lv = attr.new local_variable();
			for (int i = 0; i < vec.size(); i++) {
				short val = ((Integer) vec.get(i)).shortValue();
				if (!lv.isDefined(LocalVariableTable.local_variable.START_PC)) {
					lv.setStartPC(val);
				} else if (!lv.isDefined(LocalVariableTable.local_variable.LENGTH)) {
					lv.setLength(val);
				} else if (!lv.isDefined(LocalVariableTable.local_variable.NAME_INDEX)) {
					lv.setNameIndex(val);
				} else if (!lv.isDefined(LocalVariableTable.local_variable.DESCRIPTOR_INDEX)) {
					lv.setDescriptorIndex(val);
				} else if (!lv.isDefined(LocalVariableTable.local_variable.INDEX)) {
					lv.setIndex(val);
				} else {
					attr.addLocalVariable(lv);
					lv = attr.new local_variable();
					lv.setStartPC(val);
				}
			}
			attr.addLocalVariable(lv);
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("local_variable_table"));
		}
	}

	protected LocalVariableTypeTable parseAttrLocalVariableTypeTable()
	throws IOException, CodeFileFormatException, NumberFormatException {
	
		if (isNextTokenLeftBracket()) {
			LocalVariableTypeTable attr = new LocalVariableTypeTable();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute_name_index")) {
						attr.setAttributeNameIndex(parseNumber().shortValue());
					} else if (str.equals("attribute_length")) {
						attr.setAttributeLength(parseNumber().intValue());
					} else if (str.equals("local_variable_type_table_length")) {
						attr.setLocalVariableTypeTableLength(parseNumber().shortValue());
					} else if (str.equals("local_variable_type_table")) {
						parseAttrLocalVariableTypeTableContent(attr);
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) + 
						                                  errs[E_ILL_PAR_ATR] +
						                                  Utils.quote("LocalVariableTypeTable"));
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_PAR_ATR_EXP]);
				}
			}	
			return attr;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute LocalVariableTypeTable"));
		}
	}
	
	protected void parseAttrLocalVariableTypeTableContent(LocalVariableTypeTable attr)
	throws IOException, CodeFileFormatException, NumberFormatException {

		if (isNextTokenLeftBracket()) {
			ArrayList vec = new ArrayList();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (Utils.isHex(str)) {
						int dec = Utils.hexToDec(str);
						vec.add(new Integer(dec));
					} else if (Utils.isCPRef(str)) {
						short ref = Utils.cpRefValue(str);
						vec.add(new Integer((int)ref));
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_NOT_HEX_CPR]);
					}
				} else if (isNumber(token)) {
					vec.add(new Integer((int) st.nval));
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_DEC_HEX_CPR_EXP]);
				}
			}
			
			LocalVariableTypeTable.local_variable lv = attr.new local_variable();
			for (int i = 0; i < vec.size(); i++) {
				short val = ((Integer) vec.get(i)).shortValue();
				if (!lv.isDefined(LocalVariableTypeTable.local_variable.START_PC)) {
					lv.setStartPC(val);
				} else if (!lv.isDefined(LocalVariableTypeTable.local_variable.LENGTH)) {
					lv.setLength(val);
				} else if (!lv.isDefined(LocalVariableTypeTable.local_variable.NAME_INDEX)) {
					lv.setNameIndex(val);
				} else if (!lv.isDefined(LocalVariableTypeTable.local_variable.SIGNATURE_INDEX)) {
					lv.setSignatureIndex(val);
				} else if (!lv.isDefined(LocalVariableTypeTable.local_variable.INDEX)) {
					lv.setIndex(val);
				} else {
					attr.addLocalVariable(lv);
					lv = attr.new local_variable();
					lv.setStartPC(val);
				}
			}
			attr.addLocalVariable(lv);
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("local_variable_type_table"));
		}
	}
	
	protected InnerClasses parseAttrInnerClasses()
	throws IOException, CodeFileFormatException, NumberFormatException {
	
		if (isNextTokenLeftBracket()) {
			InnerClasses attr = new InnerClasses();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("attribute_name_index")) {
						attr.setAttributeNameIndex(parseNumber().shortValue());
					} else if (str.equals("attribute_length")) {
						attr.setAttributeLength(parseNumber().intValue());
					} else if (str.equals("number_of_classes")) {
						attr.setNumberOfClasses(parseNumber().shortValue());
					} else if (str.equals("InnerClass")) {
						parseAttrInnerClassesClass(attr);
					} else {
						throw new CodeFileFormatException(st.lineno(),
						                                  Utils.quote(str) +
						                                  errs[E_ILL_PAR_ATR] +
						                                  Utils.quote("LocalVariableTable"));
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_PAR_ATR_EXP]);
				}
			}	
			return attr;
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("attribute InnerClasses"));
		}
	}

	protected void parseAttrInnerClassesClass(InnerClasses attr)
	throws IOException, CodeFileFormatException, NumberFormatException {
		
		if (isNextTokenLeftBracket()) {
			InnerClasses.inner_class ic = attr.new inner_class();
			while (isNextTokenNotRightBracket()) {
				if (isEOL(token)) {
				} else if (isWord(token)) {
					String str = st.sval;
					if (str.equals("inner_class_info_index")) {
						ic.setInnerClassInfoIndex(parseNumber().shortValue());
					} else if (str.equals("outer_class_info_index")) {
						ic.setOuterClassInfoIndex(parseNumber().shortValue());
					} else if (str.equals("inner_name_index")) {
						ic.setInnerNameIndex(parseNumber().shortValue());
					} else if (str.equals("inner_class_access_flags")) {
						ic.setInnerClassAccessFlags(parseAccessFlags("inner_class_access_flags", 
                                                                     InnerClassModifiers.innerClassModifiers));
					} else {
						throw new CodeFileFormatException(st.lineno(), 
						                                  Utils.quote(str) +
						                                  errs[E_ILL_PAR_IC]);
					}
				} else {
					throw new CodeFileFormatException(st.lineno(),
					                                  errs[E_PAR_IC_EXP]);
				}
			}
			attr.addClass(ic);
		} else {
			throw new CodeFileFormatException(st.lineno(),
			                                  Utils.quote(CC_LEFT_CBRACKET) +
			                                  errs[E_EXP_AFTER] +
			                                  Utils.quote("InnerClass"));
		}
	}
	
	private boolean isNextTokenLeftBracket() 
		throws IOException {
		do {
			token = st.nextToken();
		} while (isEOL(token));
		return (token == CC_LEFT_CBRACKET);
	}
	
	private boolean isNextTokenNotRightBracket() 
		throws CodeFileFormatException, IOException {
		token = st.nextToken();
		if(token == StreamTokenizer.TT_EOF) {
			throw new CodeFileFormatException(st.lineno(), "Unexpected end of file.");
		}
		return (token != CC_RIGHT_CBRACKET);
		
	}
	
	
	private boolean isNextToken(int val)
		throws IOException {

		token = st.nextToken();
		if (token == val) {
			return true;
		}
		return false;
	}
	
	private boolean isWord(int token) {
		return (token == StreamTokenizer.TT_WORD);
	}

	private boolean isNumber(int token) {
		return (token == StreamTokenizer.TT_NUMBER);
	}
	
	/*private void skipLFSafe() 
		throws IOException {
		while(isEOL(token)) {
			token= st.nextToken();
		}
	}*/
	
	private void skipLF() 
		throws IOException {
		do {
			token= st.nextToken();
		}
		while( isEOL(token));
	}
	
	private boolean isEOL(int token) {
		return (token == StreamTokenizer.TT_EOL);
	}
	
	private void setupSyntax() {
		st.slashSlashComments(true);
		st.slashStarComments(true);
		st.eolIsSignificant(true);
		st.wordChars(CC_LOW_LINE, CC_LOW_LINE);
		st.wordChars(CC_NUMBER, CC_NUMBER);
		st.ordinaryChar(CC_LEFT_CBRACKET);
		st.ordinaryChar(CC_RIGHT_CBRACKET);
		st.ordinaryChar(CC_EQUALS);
		st.quoteChar(CC_QUOTATION);
	}
}
