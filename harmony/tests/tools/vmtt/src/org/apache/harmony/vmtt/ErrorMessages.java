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
 * Created on 20.12.2004
 */

package org.apache.harmony.vmtt;

/**
 * @author agolubit
 */

public interface ErrorMessages {

    String[] errs = {
        " is expected",
        " is expected after ",
        "Constant is expected",
        "Type of code is expected",
        "Decimal or hexadecimal number is expected",
        "Decimal, hexadecimal number or constant pool reference is expected",
        "Reference to the constant pool is expected",
        "Field is expected: field { ... }",
        "Method is expected: method { ... }",
        "Attribute is expected: attribute { ... }",
        "Hexadecimal number is expected",
        "Modifier or hexadecimal number is expected",
        "Quoted string is expected",
        "VM instruction is expected",
        "Parameter of the field is expected",
        "Parameter of the method is expected",
        "Parameter of the attribute is expected",
        "Parameter of the inner class is expected",
        " can not be parsed as hexadecimal number",
        " can not be parsed as hexadecimal number or constant pool reference",
        " can not be parsed as constant pool reference",
        " - is illegal VM instruction ",
        " - is illegal modifier ",
        " - is illegal parameter of the field ",
        " - is illegal parameter of the method ",
        " - is illegal parameter of the attribute ",
        " - is illegal type of the attribute ",
        " - is illegal parameter of the inner class ",
        " - is illegal type of the code ",
        " - is illegal type of the constant ",
        " - is illegal keyword ",
        "File not found ",
        "I/O error occurs while writing file ",
        "I/O error occurs while reading file ",
        "Default class file parser not found.",
        "Default class file generator not found.",
        "Default ccode file parser not found.",
        "Default ccode file generator not found.",
        "Lable is expected."
    	};

    int E_EXP				= 0;
    int E_EXP_AFTER			= 1;
    int E_CONST_EXP			= 2;
    int E_TP_COD_EXP		= 3;
    int E_DEC_HEX_EXP		= 4;
    int E_DEC_HEX_CPR_EXP	= 5;
    int E_CPR_EXP			= 6;
    int E_FLD_EXP			= 7;
    int E_MTD_EXP			= 8;
    int E_ATR_EXP			= 9;
    int E_HEX_EXP			= 10;
    int E_MOD_HEX_EXP		= 11;
    int E_QUOT_EXP			= 12;
    int E_VMI_EXP			= 13;
    int E_PAR_FLD_EXP		= 14;
    int E_PAR_MTD_EXP		= 15;
    int E_PAR_ATR_EXP		= 16;
    int E_PAR_IC_EXP		= 17;
    int E_NOT_HEX			= 18;
    int E_NOT_HEX_CPR		= 19;
    int E_NOT_CPR			= 20;
    int E_ILL_VMI			= 21;
    int E_ILL_MOD			= 22;
    int E_ILL_PAR_FLD		= 23;
    int E_ILL_PAR_MTD		= 24;
    int E_ILL_PAR_ATR		= 25;
    int E_ILL_TP_ATR		= 26;
    int E_ILL_PAR_IC		= 27; 
    int E_ILL_TP_COD		= 28;
    int E_ILL_TP_CON		= 29;
    int E_ILL_KWRD			= 30; 
    int E_FILE_NF			= 31;
    int E_IO_WRITE			= 32;
    int E_IO_READ			= 33;
    int E_DCLASS_PAR_NF		= 34;
    int E_DCLASS_GEN_NF		= 35;
    int E_DCCODE_PAR_NF		= 36;
    int E_DCCODE_GEN_NF		= 37;
    int E_LBL_EXP			= 38;
}
