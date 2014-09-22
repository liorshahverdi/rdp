import java.util.*;
import java.io.*;

public class Parser{
		
	private static Scanner file = null;
	private static String nextStr;
	private static Token nextToken;

	public Parser(String filename)
	{
		try
		{
			file = new Scanner(new File(filename));
		} catch (FileNotFoundException e)
		{
			System.out.println("File "+filename+" is not found");
			System.exit(0);
		}
		getNextToken();
		commenceParsing();
	}

	public enum Token{
		CONSTANT("const"), VARIABLE("var"), PROCEDURE("procedure"), CALL("call"), BEGIN("begin"), END("end"),
		IF("if"), THEN("then"), ELSE("else"), WHILE("while"), DO("do"), OPENPAREN("("), CLOSEPAREN(")"),
 		COMMA(","), SEMICOLON(";"), ASSIGN_EQUAL(":="),
 		EQUAL("="), NOT_EQUAL("!="), LESS_THAN("<"), GREATER_THAN(">"), LESS_THAN_EQUAL_TO("<="), GREATER_THAN_EQUAL_TO(">="),
		PLUS("+"), MINUS("-"), MULTIPLY("*"), DIVIDE("/"),
		NUMBER("0110"),
 		ZERO("0"), ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"),
 		LOW_A("a"), LOW_B("b"), LOW_C("c"), LOW_D("d"), LOW_E("e"), LOW_F("f"), LOW_G("g"), LOW_H("h"), LOW_I("i"),
 		LOW_J("j"), LOW_K("k"), LOW_L("l"), LOW_M("m"), LOW_N("n"), LOW_O("o"), LOW_P("p"), LOW_Q("q"), LOW_R("r"), LOW_S("s"),
 		LOW_T("t"), LOW_U("u"), LOW_V("v"), LOW_W("w"), LOW_X("x"), LOW_Y("y"), LOW_Z("z"),
 		UP_A("A"), UP_B("B"), UP_C("C"), UP_D("D"), UP_E("E"), UP_F("F"), UP_G("G"), UP_H("H"), UP_I("I"), UP_J("J"), UP_K("K"),
 		UP_L("L"), UP_M("M"), UP_N("N"), UP_O("O"), UP_P("P"), UP_Q("Q"), UP_R("R"), UP_S("S"), UP_T("T"), UP_U("U"), UP_V("V"), UP_W("W"),
 		UP_X("X"), UP_Y("Y"), UP_Z("Z"),
 		USER_DEFINED_NAME("---"),
 		END_OF_INPUT("$");
		
		private String str;

		private Token(String s){
			this.str = s;
		}

		public String getStr(){
    		return str;
		}

		public static Token getEnum(String str) {
			for (Token t: Token.values()){
				if (str.equals(t.getStr())){
					return t;
				}
			}
			return null;
		}

		/*public static Token getEnum(String str){
			switch (str) {
		        case "const":
		            return CONSTANT;
		        case "var":
		        	return VARIABLE;
		        case "procedure":
		        	return PROCEDURE;
		        case "call":
		        	return CALL;
		        case "begin":
		        	return BEGIN;
		        case "end":
		        	return END;
		        case "if":
		        	return IF;
		        case "then":
		        	return THEN;
		        case "else":
		        	return ELSE;
		        case "while":
		        	return WHILE;
		        case "do":
		        	return DO;
		        case "(":
		        	return OPENPAREN;
		        case ")":
					return CLOSEPAREN;
		        case ",":
		        	return COMMA;
		        case ";":
		        	return SEMICOLON;
		        case ":=":
		        	return ASSIGN_EQUAL;
		        case "=":
		        	return EQUAL;
		        case "!=":
		        	return NOT_EQUAL;
		        case "<":
		        	return LESS_THAN;
		        case ">":
		        	return GREATER_THAN;
		        case "<=":
		        	return LESS_THAN_EQUAL_TO;
		        case ">=":
		        	return GREATER_THAN_EQUAL_TO;
		        case "+":
		        	return PLUS;
		        case "-":
		        	return MINUS;
		        case "*":
		        	return MULTIPLY;
		        case "/":
		        	return DIVIDE;
		        case "0":
		        	return ZERO;
		        case "1":
		        	return ONE;
		        case "2":
		        	return TWO;
		        case "3":
		        	return THREE;
		        case "4":
		        	return FOUR;
		        case "5":
		        	return FIVE;
		        case "6":
		        	return SIX;
		        case "7":
		        	return SEVEN;
		        case "8":
		        	return EIGHT;
		        case "9":
		        	return NINE;
		        case "a":
		        	return LOW_A;
		        case "b":
		        	return LOW_B;
		        case "c":
		        	return LOW_C;
		        case "d":
		        	return LOW_D;
		        case "e":
		        	return LOW_E;
		        case "f":
		        	return LOW_F;
		        case "g":
		        	return LOW_G;
		        case "h":
		        	return LOW_H;
		        case "i":
		        	return LOW_I;
		        case "j":
		        	return LOW_J;
		        case "k":
		        	return LOW_K;
		        case "l":
		        	return LOW_L;
		        case "m":
		        	return LOW_M;
		        case "n":
		        	return LOW_N;
		        case "o":
		        	return LOW_O;
		        case "p":
		        	return LOW_P;
		        case "q":
		        	return LOW_Q;
		        case "r":
		        	return LOW_R;
		        case "s":
		        	return LOW_S;
		        case "t":
		        	return LOW_T;
		        case "u":
		        	return LOW_U;
		        case "v":
		        	return LOW_V;
		        case "w":
		        	return LOW_W;
		        case "x":
		        	return LOW_X;
		        case "y":
		        	return LOW_Y;
		        case "z":
		        	return LOW_Z;
		        case "A":
		        	return UP_A;
		        case "B":
		        	return UP_B;
		        case "C":
		        	return UP_C;
		        case "D":
		        	return UP_D;
		        case "E":
		        	return UP_E;
		        case "F":
		        	return UP_F;
		        case "G":
		        	return UP_G;
		        case "H":
		        	return UP_H;
		        case "I":
		        	return UP_I;
		        case "J":
		        	return UP_J;
		        case "K":
		        	return UP_K;
		        case "L":
		        	return UP_L;
		        case "M":
		        	return UP_M;
		        case "N":
		        	return UP_N;
		        case "O":
		        	return UP_O;
		        case "P":
		        	return UP_P;
		        case "Q":
		        	return UP_Q;
		        case "R":
		        	return UP_R;
		        case "S":
		        	return UP_S;	
		        case "T":
		        	return UP_T;
		        case "U":
		        	return UP_U;
		        case "V":
		        	return UP_V;
		        case "W":
		        	return UP_W;
		        case "X":
		        	return UP_X;
		       	case "Y":
		       		return UP_Y;
		       	case "Z":
		       		return UP_Z;
		        default:
		            return null;
		    }
		}*/
	}

	public static void commenceParsing(){
		if (program()){
			System.out.println("Program Complete!");
		}
		else {
			System.out.println("Error!\tnextStr = "+nextStr);
			System.exit(0);
		}
	}

	public static void getNextToken(){
		nextStr = file.next();
		nextToken = Token.getEnum(nextStr);
		System.out.println("**\t"+nextToken);
		if (nextStr.matches("[a-zA-Z]{1}[\\w]*")) nextToken = Token.USER_DEFINED_NAME;
		if (nextStr.matches("[0-9]*")) nextToken = Token.NUMBER;
		System.out.println("**\t"+nextToken);
		if (nextToken == null){
			//didn't find proper enum
			System.out.println("INVALID ==> "+nextStr);
			System.exit(0);	
		}
	}

	public static boolean letter() {
		switch (nextToken) {
			case UP_A: case UP_B: case UP_C:
			case UP_D: case UP_E: case UP_F:
			case UP_G: case UP_H: case UP_I:
			case UP_J: case UP_K: case UP_L:
			case UP_M: case UP_N: case UP_O:
			case UP_P: case UP_Q: case UP_R:
			case UP_S: case UP_T: case UP_U:
			case UP_V: case UP_W: case UP_X:
			case UP_Y: case UP_Z:
			case LOW_A: case LOW_B: case LOW_C:
			case LOW_D: case LOW_E: case LOW_F:
			case LOW_G: case LOW_H: case LOW_I:
			case LOW_J: case LOW_K: case LOW_L:
			case LOW_M: case LOW_N: case LOW_O:
			case LOW_P: case LOW_Q: case LOW_R:
			case LOW_S: case LOW_T: case LOW_U:
			case LOW_V: case LOW_W: case LOW_X:
			case LOW_Y: case LOW_Z:
			{
				getNextToken();
				return true;
			}	
			default: {
				return false;
			}
		}
	}

	public static boolean digit() {
		switch (nextToken) {
			case ZERO: case ONE: case TWO: case THREE: case FOUR:
			case FIVE: case SIX: case SEVEN: case EIGHT: case NINE:
			{
				getNextToken();
				return true;
			}	
			default: {
				return false;
			}
		}
	}

	public static boolean number() {
		/*if (digit()) {
			while (digit()) {
				continue;
			}
			return true;
		}
		else return false;*/
		return true;
	}

	public static boolean ident() {
		/*if (letter()) {
			while ( letter() || digit() ) {
				continue;
			}
			return true;
		}
		else return false;*/
		getNextToken();
		return true;
	}

	public static boolean factor() {
		if (ident()) {
			return true;
		}
		else if (number()) {
			return true;
		}
		else if (nextToken == Token.OPENPAREN){
			getNextToken();
			if (expression()) {
				if (nextToken == Token.CLOSEPAREN){
					getNextToken();
					return true;
				}
				else return false;
			}
			else return false;
		}
		else return false;
	}

	public static boolean term() {
		if (factor()) {
			while (nextToken == Token.MULTIPLY || nextToken == Token.DIVIDE) {
				getNextToken();
				if (factor()) {
					continue;
				}
				else return false;
			}
			return true;
		}
		else return false;
	}

	public static boolean expression() {
		System.out.println("Made it to expression!");
		if (nextToken == Token.PLUS){
			getNextToken();
		}
		else if (nextToken == Token.MINUS){
			getNextToken();
		} 
		if (term()) {
			while (nextToken == Token.PLUS || nextToken == Token.MINUS){
				getNextToken();
				if (term()){
					continue;
				}
				else return false;
			}
			System.out.println("True indeed");
			return true;
		}
		else return false;
	}

	public static boolean relOp(){
		switch (nextToken) {
			case EQUAL: case NOT_EQUAL: case LESS_THAN:
			case GREATER_THAN: case LESS_THAN_EQUAL_TO: case GREATER_THAN_EQUAL_TO: {
				getNextToken();
				return true;
			}
			default: {
				return false;
			}
		}
	}

	public static boolean condition() {
		if (expression()){
			if (relOp()) {
				if (expression()){
					return true;
				}
				else return false;
			}
			else return false;
		}
		else return false;
	}
	
	public static boolean iterativeStat() {
		if (nextToken == Token.WHILE){
			getNextToken();
			if (condition()) {
				if (nextToken == Token.DO){
					getNextToken();
					if (statement()){
						return true;
					}
					else return false;
				}
				else return false;
			}
			else return false;
		}
		else return false;
	}

	public static boolean selectionStat() {
		if (nextToken == Token.IF){
			getNextToken();
			if (condition()) {
				if (nextToken == Token.THEN) {
					getNextToken();
					if (statement()) {
						if (nextToken == Token.ELSE) {
							getNextToken();
							if (statement()){
								return true;
							}
							else return false;
						}
						else return false;
					}
					else return false;
				}
				else return false;
			}
			else return false;
		}
		else return false;
	}
	
	public static boolean compoundStat() { 
		if (nextToken == Token.BEGIN) {
			getNextToken();
			if (statement()) {
				while(nextToken == Token.SEMICOLON){
					getNextToken();
					if (statement()){
						continue;
					}
				}
				if (nextToken == Token.END){
					getNextToken();
					return true;
				}
				else return false;
			}
			else return false;
		}
		else return false; 
	}
	
	public static boolean procedureCallStat(){
		if (nextToken == Token.CALL){
			getNextToken();
			if (ident()) {
				return true;
			}
			else return false;
		}
		else return false;
	}

	public static boolean assignmentStat(){
		if (ident()){
			
			if (nextToken == Token.ASSIGN_EQUAL){
				System.out.println("Here too!");
				getNextToken();
				if (expression()){
					System.out.println("expressions arent always true, but today they are");
					return true;
				}
				else return false;
			}
			else return false;
		}
		else return false;
	}

	public static boolean statement() {
		if (assignmentStat()) { 
			System.out.println("Assignment Statement indeed!");
			return true; 
		}
		else if (procedureCallStat()) { 
			System.out.println("Procedure Call Statement Indeed!");
			return true; }
		else if (compoundStat()) { return true; }
		else if (selectionStat()) { return true; }
		else if (iterativeStat()) { return true; }
		else return false;
	}

	public static boolean block(){
		if (nextToken == Token.CONSTANT) {
			getNextToken();
			if (ident()) {
				if (nextToken == Token.EQUAL){
					getNextToken();
					if (number()){
						while (nextToken == Token.COMMA){
							getNextToken();
							if (ident()){
								if (nextToken == Token.EQUAL){
									if (number()){
										continue;
									}
								}	
							}
						}
						if (nextToken == Token.SEMICOLON){
							getNextToken();
						}
					}
				}
			}
		}
		if (nextToken == Token.VARIABLE){
			getNextToken();
			if (ident()){
				while (nextToken == Token.COMMA){
					getNextToken();
					if (ident()){
						continue;
					}
				}
				if (nextToken == Token.SEMICOLON){
					getNextToken();
				}				
			}
		}
		while (nextToken == Token.PROCEDURE){
			getNextToken();
			if (ident()) {
				if (nextToken == Token.SEMICOLON){
					getNextToken();
					if (block()) {
						continue;
					}
				}
			}
		}
		System.out.println("nextToken is now this here\t"+nextStr);

		if (statement()) {
			System.out.println("Statement went through!\tnextStr = "+nextStr);
			return true;
		}
		else {
			System.out.println("Statement syntax error!\t"+nextStr);
			return false;
		}
	}

	public static boolean program(){
		if (block()){
			if (nextToken == Token.END_OF_INPUT) return true;
			else {
				System.out.println("Missing end of input!token="+nextToken)
				return false;
			}
		}
		else return false;
	}

	/*public class ParseException extends Exception{
		public static ParseException(String s) { super(s); }
	}*/
	
	public static void main(String[] args){
		System.out.print("Enter a filename.. ");
		Scanner input = new Scanner(System.in);
		String inStr = input.next();
		Parser p = new Parser(inStr);
	}
}