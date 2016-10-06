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
package org.apache.harmony.vmtt.disasm;

import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Vector;

import org.apache.harmony.vmtt.VMTT;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.3 $
 */

public class InstructionsTable {
	
	private static final byte _ib = -1;
	
	private static final Instruction[] instructions = {
/*
 * All instructions' mnemonics and opcodes' values are taken from 
 * "The Java (TM) Virtual Machine Specification Second Edition"
 * available at http://java.sun.org/docs/books/vmspec/2nd-edition/html/VMSpecTOC.doc.html 
 * 
 */
			new SimpleInstruction(0, "nop"),

			new SimpleInstruction(1, "aconst_null"),

			/* iconst_N instructions */
			new SimpleInstruction(2, "iconst_m1"),
			new SimpleInstruction(3, "iconst_0"),
			new SimpleInstruction(4, "iconst_1"),
			new SimpleInstruction(5, "iconst_2"),
			new SimpleInstruction(6, "iconst_3"),
			new SimpleInstruction(7, "iconst_4"),
			new SimpleInstruction(8, "iconst_5"),

			/* lconst_N instructions */
			new SimpleInstruction(9, "lconst_0"),
			new SimpleInstruction(10, "lconst_1"),

			/* fconst_N instructions */
			new SimpleInstruction(11, "fconst_0"),
			new SimpleInstruction(12, "fconst_1"),
			new SimpleInstruction(13, "fconst_2"),

			/* dconst_N instructions */
			new SimpleInstruction(14, "dconst_0"),
			new SimpleInstruction(15, "dconst_1"),

			/* **push instructions */
			new SimpleInstruction(16, "bipush", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),
			new SimpleInstruction(17, "sipush", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),

			/* ldc instructions */
			new SimpleInstruction(18, "ldc", new Operand[] {new Operand(Operand.OT_REF, new Byte(_ib))}),
			new SimpleInstruction(19, "ldc_w", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),
			new SimpleInstruction(20, "ldc2_w", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),

			/* *load instructions */
			new SimpleInstruction(21, "iload", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),
			new SimpleInstruction(22, "lload", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),
			new SimpleInstruction(23, "fload", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),
			new SimpleInstruction(24, "dload", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),
			new SimpleInstruction(25, "aload", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),

			/* iload_N instructions */
			new SimpleInstruction(26, "iload_0"),
			new SimpleInstruction(27, "iload_1"),
			new SimpleInstruction(28, "iload_2"),
			new SimpleInstruction(29, "iload_3"),

			/* lload_N instructions */
			new SimpleInstruction(30, "lload_0"),
			new SimpleInstruction(31, "lload_1"),
			new SimpleInstruction(32, "lload_2"),
			new SimpleInstruction(33, "lload_3"),

			/* fload_N instructions */
			new SimpleInstruction(34, "fload_0"),
			new SimpleInstruction(35, "fload_1"),
			new SimpleInstruction(36, "fload_2"),
			new SimpleInstruction(37, "fload_3"),

			/* dload_N instructions */
			new SimpleInstruction(38, "dload_0"),
			new SimpleInstruction(39, "dload_1"),
			new SimpleInstruction(40, "dload_2"),
			new SimpleInstruction(41, "dload_3"),

			/* aload_N instructions */
			new SimpleInstruction(42, "aload_0"),
			new SimpleInstruction(43, "aload_1"),
			new SimpleInstruction(44, "aload_2"),
			new SimpleInstruction(45, "aload_3"),

			/* aload instructions */
			new SimpleInstruction(46, "iaload"),
			new SimpleInstruction(47, "laload"),
			new SimpleInstruction(48, "faload"),
			new SimpleInstruction(49, "daload"),
			new SimpleInstruction(50, "aaload"),
			new SimpleInstruction(51, "baload"),
			new SimpleInstruction(52, "caload"),
			new SimpleInstruction(53, "saload"),

			/* *store instructions */
			new SimpleInstruction(54, "istore", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),
			new SimpleInstruction(55, "lstore", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),
			new SimpleInstruction(56, "fstore", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),
			new SimpleInstruction(57, "dstore", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),
			new SimpleInstruction(58, "astore", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),

			/* istore_N instructions */
			new SimpleInstruction(59, "istore_0"),
			new SimpleInstruction(60, "istore_1"),
			new SimpleInstruction(61, "istore_2"),
			new SimpleInstruction(62, "istore_3"),

			/* lstore_N instructions */
			new SimpleInstruction(63, "lstore_0"),
			new SimpleInstruction(64, "lstore_1"),
			new SimpleInstruction(65, "lstore_2"),
			new SimpleInstruction(66, "lstore_3"),

			/* fstore_N instructions */
			new SimpleInstruction(67, "fstore_0"),
			new SimpleInstruction(68, "fstore_1"),
			new SimpleInstruction(69, "fstore_2"),
			new SimpleInstruction(70, "fstore_3"),

			/* dstore_N instructions */
			new SimpleInstruction(71, "dstore_0"),
			new SimpleInstruction(72, "dstore_1"),
			new SimpleInstruction(73, "dstore_2"),
			new SimpleInstruction(74, "dstore_3"),

			/* astore_N instructions */
			new SimpleInstruction(75, "astore_0"),
			new SimpleInstruction(76, "astore_1"),
			new SimpleInstruction(77, "astore_2"),
			new SimpleInstruction(78, "astore_3"),

			/* *astore instructions */
			new SimpleInstruction(79, "iastore"),
			new SimpleInstruction(80, "lastore"),
			new SimpleInstruction(81, "fastore"),
			new SimpleInstruction(82, "dastore"),
			new SimpleInstruction(83, "aastore"),
			new SimpleInstruction(84, "bastore"),
			new SimpleInstruction(85, "castore"),
			new SimpleInstruction(86, "sastore"),
			
			/* pop instructions */
			new SimpleInstruction(87, "pop"),
			new SimpleInstruction(88, "pop2"),
			
			/* dup instructions */
			new SimpleInstruction(89, "dup"),
			new SimpleInstruction(90, "dup_x1"),
			new SimpleInstruction(91, "dup_x2"),

			/* dup2 instructions */
			new SimpleInstruction(92, "dup2"),
			new SimpleInstruction(93, "dup2_x1"),
			new SimpleInstruction(94, "dup2_x2"),

			/* swap instruction */
			new SimpleInstruction(95, "swap"),
			
			/* *add instructions */
			new SimpleInstruction(96, "iadd"),
			new SimpleInstruction(97, "ladd"),
			new SimpleInstruction(98, "fadd"),
			new SimpleInstruction(99, "dadd"),

			/* *sub instructions */
			new SimpleInstruction(100, "isub"),
			new SimpleInstruction(101, "lsub"),
			new SimpleInstruction(102, "fsub"),
			new SimpleInstruction(103, "dsub"),

			/* *mul instructions */
			new SimpleInstruction(104, "imul"),
			new SimpleInstruction(105, "lmul"),
			new SimpleInstruction(106, "fmul"),
			new SimpleInstruction(107, "dmul"),

			/* *div instructions */
			new SimpleInstruction(108, "idiv"),
			new SimpleInstruction(109, "ldiv"),
			new SimpleInstruction(110, "fdiv"),
			new SimpleInstruction(111, "ddiv"),
			
			/* *rem instructions */
			new SimpleInstruction(112, "irem"),
			new SimpleInstruction(113, "lrem"),
			new SimpleInstruction(114, "frem"),
			new SimpleInstruction(115, "drem"),

			/* *neg instructions */
			new SimpleInstruction(116, "ineg"),
			new SimpleInstruction(117, "lneg"),
			new SimpleInstruction(118, "fneg"),
			new SimpleInstruction(119, "dneg"),

			/* *shl instructions */
			new SimpleInstruction(120, "ishl"),
			new SimpleInstruction(121, "lshl"),

			/* *shr instructions */
			new SimpleInstruction(122, "ishr"),
			new SimpleInstruction(123, "lshr"),

			/* *ushr instructions */
			new SimpleInstruction(124, "iushr"),
			new SimpleInstruction(125, "lushr"),

			/* *and instructions */
			new SimpleInstruction(126, "iand"),
			new SimpleInstruction(127, "land"),

			/* *or instructions */
			new SimpleInstruction(128, "ior"),
			new SimpleInstruction(129, "lor"),

			/* *xor instructions */
			new SimpleInstruction(130, "ixor"),
			new SimpleInstruction(131, "lxor"),

			/* iinc instruction */
			new SimpleInstruction(132, "iinc", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib)),
														new Operand(Operand.OT_VAL, new Byte(_ib))}),

			/* i2* instructions */
			new SimpleInstruction(133, "i2l"),
			new SimpleInstruction(134, "i2f"),
			new SimpleInstruction(135, "i2d"),
			
			/* l2* instructions */
			new SimpleInstruction(136, "l2i"),
			new SimpleInstruction(137, "l2f"),
			new SimpleInstruction(138, "l2d"),

			/* f2* instructions */
			new SimpleInstruction(139, "f2i"),
			new SimpleInstruction(140, "f2l"),
			new SimpleInstruction(141, "f2d"),

			/* d2* instructions */
			new SimpleInstruction(142, "d2i"),
			new SimpleInstruction(143, "d2l"),
			new SimpleInstruction(144, "d2f"),

			/* i2* instructions */
			new SimpleInstruction(145, "i2b"),
			new SimpleInstruction(146, "i2c"),
			new SimpleInstruction(147, "i2s"),

			/* lcmp instruction */
			new SimpleInstruction(148, "lcmp"),

			/* fcmp* instructions */
			new SimpleInstruction(149, "fcmpl"),
			new SimpleInstruction(150, "fcmpg"),

			/* dcmp* instructions */
			new SimpleInstruction(151, "dcmpl"),
			new SimpleInstruction(152, "dcmpg"),
			
			/* if** instructions */
			new SimpleInstruction(153, "ifeq", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(154, "ifne", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(155, "iflt", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(156, "ifge", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(157, "ifgt", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(158, "ifle", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			
			/* if_icmp** instructions */
			new SimpleInstruction(159, "if_icmpeq", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(160, "if_icmpne", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(161, "if_icmplt", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(162, "if_icmpge", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(163, "if_icmpgt", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(164, "if_icmple", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),

			/* if_acmp** instructions */
			new SimpleInstruction(165, "if_acmpeq", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(166, "if_acmpne", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),

			new SimpleInstruction(167, "goto", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),

			new SimpleInstruction(168, "jsr", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),

			new SimpleInstruction(169, "ret", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),

			/* switch instructions */
			new TableSwitchInstruction(),
			new LookupSwitchInstruction(),
			
			/* *return instructions */
			new SimpleInstruction(172, "ireturn"),
			new SimpleInstruction(173, "lreturn"),
			new SimpleInstruction(174, "freturn"),
			new SimpleInstruction(175, "dreturn"),
			new SimpleInstruction(176, "areturn"),
			new SimpleInstruction(177, "return"),
			
			/* ***static instructions */
			new SimpleInstruction(178, "getstatic", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),
			new SimpleInstruction(179, "putstatic", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),
			
			/* ***field instructions */
			new SimpleInstruction(180, "getfield", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),
			new SimpleInstruction(181, "putfield", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),

			/* invoke* instructions */
			new SimpleInstruction(182, "invokevirtual", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),
			new SimpleInstruction(183, "invokespecial", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),
			new SimpleInstruction(184, "invokestatic", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),
			new SimpleInstruction(185, "invokeinterface", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib)),
																   new Operand(Operand.OT_VAL, new Byte(_ib)),
																   new Operand(Operand.OT_VAL, new Byte(_ib))}),

			/* new instructions */
			new SimpleInstruction(187, "new", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),
			new SimpleInstruction(188, "newarray", new Operand[] {new Operand(Operand.OT_VAL, new Byte(_ib))}),
			new SimpleInstruction(189, "anewarray", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),

			/* arraylength instruction */
			new SimpleInstruction(190, "arraylength"),

			new SimpleInstruction(191, "athrow"),

			new SimpleInstruction(192, "checkcast", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),

			new SimpleInstruction(193, "instanceof", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib))}),

			/* monitor instructions */
			new SimpleInstruction(194, "monitorenter"),
			new SimpleInstruction(195, "monitorexit"),

			/* wide instruction */
			new WideInstruction(),

			new SimpleInstruction(197, "multianewarray", new Operand[] {new Operand(Operand.OT_REF, new Short(_ib)),
																  new Operand(Operand.OT_VAL, new Byte(_ib))}),

			new SimpleInstruction(198, "ifnull", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),
			new SimpleInstruction(199, "ifnonnull", new Operand[] {new Operand(Operand.OT_VAL, new Short(_ib))}),

			new SimpleInstruction(200, "goto_w", new Operand[] {new Operand(Operand.OT_VAL, new Integer(_ib))}),
			new SimpleInstruction(201, "jsr_w", new Operand[] {new Operand(Operand.OT_VAL, new Integer(_ib))})
	};

	public static Instruction getInstruction(int opcode) {
		for (int i = 0; i < instructions.length; i++) {
			if (instructions[i].getOpcode() == opcode) {
				return instructions[i];
			}
		}
		return null;
	}
	
	public static Instruction getInstruction(String mnemonic) {
	    if (mnemonic == null) {
	        throw new NullPointerException();
	    }
	    return getInstruction(getCode(mnemonic));
	}
	
	public static int getCode(String mnemonic) {
		if (mnemonic == null) {
			throw new NullPointerException();
		}
		for (int i = 0; i < instructions.length; i++) {
		    Instruction instr = instructions[i];
			if (mnemonic.equalsIgnoreCase(instr.getMnemonic())) {
				return instr.getOpcode();
			}
		}
		return -1;
	}
	
	public static String toString(byte[] code) {
		Vector vec = toStringVector(code);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < vec.size(); i++) {
			sb.append((String) vec.elementAt(i) + "\n");
		}
		return sb.toString();
	}
	
	public static Vector toStringVector(byte[] code) {
		if (code == null) {
			throw new NullPointerException();
		}
		Vector vec = new Vector();
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(code));
		int cpos = 0;
		try {
			while (true) {
				byte b = dis.readByte();
				Instruction instr = getInstruction(b & 0xFF);
				if (instr != null) {
					int ts = instr.read(dis, cpos);
					String instr_str = instr.toString();
					if (instr_str.indexOf("\n") == -1) {
						//vec.add("/*" + cpos + "*/\t" + instr_str);
					    vec.add(cpos + ":\t" + instr_str);
					} else {
						String[] smas = instr_str.split("\\n");
						//String nbytes = "/*" + cpos + "*/";
						String nbytes = cpos + ":";
						StringBuffer leftm = new StringBuffer();
						for (int l = 0; l < nbytes.length(); l++) {
							leftm.append(" ");
						}
						for (int k = 0; k < smas.length; k++) {
							if (k == 0) {
								vec.add(nbytes + "\t" + smas[k]);
							} else {
								vec.add(leftm.toString() + "\t" + smas[k]);
							}
						}
					}
					cpos += ts;
				}
				cpos++;
			}
		} catch (EOFException eofe) {
		} catch (IOException ioe) {
		    VMTT.log.warning("Error occurs while reading code data.");
		}
		return vec;
	}
}
