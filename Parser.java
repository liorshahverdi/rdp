import java.util.*;
import java.io.*;

public class Parser{
		
	private static Scanner file = null;
	private static String nextStr;
	private static Token nextToken;

	public Parser()
	{
		boolean continueScan = true;
		while (continueScan){
			System.out.print("Enter a filename..");
			Scanner inputScan = new Scanner(System.in);
			String filename = inputScan.next();
			try
			{file = new Scanner(new File(filename));} 
			catch (FileNotFoundException e)
			{
				System.out.println("File "+filename+" is not found");
				System.exit(0);
			}
			getNextToken();
			commenceParsing();
			System.out.print("\n\n\nScan again? (y/n)");
			String nextChar = inputScan.next().substring(0,1).toUpperCase();
			if (nextChar.equals("N")) continueScan = false; 
			else if (nextChar.equals("Y")) continue;
		}
		
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
 		UP_L("L"), UP_M("M"), UP_N("N"), UP_O("O"), UP_P("P"), UP_Q("Q"), UP_R("R"), UP_S("S"), UP_T("T"), UP_U("U"), UP_V("V"), 
 		UP_W("W"), UP_X("X"), UP_Y("Y"), UP_Z("Z"),
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
	}

	public static void commenceParsing(){
		if (program()){
			System.out.println("\nProgram is Syntactically Correct!");
		}
		else {
			System.out.println("Syntax Error!\tnextStr = "+nextStr);
			System.exit(0);
		}
	}

	public static void getNextToken(){
		nextStr = file.next();
		nextToken = Token.getEnum(nextStr);
		if (nextToken == null){
			if (nextStr.matches("[a-zA-Z]{1}[\\w]*")) {
				nextToken = Token.USER_DEFINED_NAME;
			}
			if (nextStr.matches("[0-9]*")) {
				nextToken = Token.NUMBER;
			}
			if (nextToken == null){
				System.out.println("INVALID ==> "+nextStr);
				System.exit(0);	
			}
		}
		//System.out.println("nt is now ==>"+nextToken );
	}

	public static boolean letter() {
		System.out.print("<letter>");
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
		System.out.print("<digit>");
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
		System.out.print("<number>");
		getNextToken();
		return true;
	}

	public static boolean ident() {
		if (nextToken == Token.USER_DEFINED_NAME){
			getNextToken();
			return true;
		}
		return false;
	}

	public static boolean factor() {
		if (ident()) {
			System.out.print("<ident>");
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
		System.out.println("Made it to expression! nt = "+nextToken);
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
					System.out.println("Proper term! nextoken=>"+nextToken);
					continue;
				}
				else return false;
			}
			System.out.println("True indeed\tnt = "+nextToken);
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
		System.out.println("<condition>");
		if (expression()){
			System.out.println("</expression>");
			if (relOp()) {
				System.out.println("<relOp>");
				if (expression()){
					System.out.println("</expression>");
					return true;
				}
				else return false;
			}
			else {
				System.out.println("relOp is NOT true! nt ="+nextToken+"\tstr = "+nextStr);
				return false;
			}
		}
		else return false;
	}
	
	public static boolean iterativeStat() {
		System.out.println("<iterativeStmt>");
		if (nextToken == Token.WHILE){
			System.out.print("<while>");
			getNextToken();
			if (condition()) {
				System.out.println("</condition>");
				if (nextToken == Token.DO){
					System.out.println("<do>");
					getNextToken();
					if (statement()){
						return true;
					}
					else {
						System.out.println(" Improper statement! \tnt = "+nextToken);
						return false;
					}
				
				}
				else return false;
			}
			else return false;
		}
		else {
			System.out.println("no while token boohoo");
			return false;
		}
	}

	public static boolean selectionStat() {
		System.out.println("selectionStat reached OOO! mytoken -> "+nextToken);
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
		else 
			{
				System.out.println("No if:( nt = "+nextToken);
				return false;
			}
	}
	
	public static boolean compoundStat() { 
		System.out.println("compoundStat reached! mytoken ->"+nextToken);
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
					System.out.println("<end />");
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
		System.out.print("<procedureCallStmt>");
		if (nextToken == Token.CALL){
			System.out.print("<call />");
			getNextToken();
			if (ident()) {
				System.out.print("<ident>");
				return true;
			}
			else return false;
		}
		else {

			System.out.println("No procedure call today\tnextStr = "+nextStr);
			return false;
		}
	}

	public static boolean assignmentStat(){
		System.out.print("<asgnStmt>");
		if (ident()){
			System.out.print("<ident>");
			if (nextToken == Token.ASSIGN_EQUAL){
				System.out.print("<:= />");
				getNextToken();
				if (expression()){
					System.out.print("</expression>");
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
			System.out.println("</asgnStmt>");
			return true; }
		else if (procedureCallStat()) { 
			System.out.println("</procedureCallStmt>");
			return true; }
		else if (compoundStat()) { 
			System.out.println("</compoundStmt>");
			return true; }
		else if (selectionStat()) { 
			System.out.println("</selectionStmt>");
			return true; }
		else if (iterativeStat()) { 
			System.out.println("</iterativeStmt>");
			return true; }

		else 
			{
				System.out.println("No statement today :((\t-->"+nextStr);
				return false;
			}
	}

	public static boolean block(){
		System.out.print("<block> ");
		if (nextToken == Token.CONSTANT) {
			System.out.print("</constant>");
			getNextToken();
			if (ident()) {
				System.out.print("</ident>");
				if (nextToken == Token.EQUAL){
					System.out.print("</equal");
					getNextToken();
					if (number()){
						System.out.print("</number>");
						while (nextToken == Token.COMMA){
							System.out.print("</comma>");
							getNextToken();
							if (ident()){
								System.out.print("</ident>");
								if (nextToken == Token.EQUAL){
									System.out.print("</equal>");
									if (number()){
										System.out.print("</number>");
										continue;
									}
								}	
							}
						}
						if (nextToken == Token.SEMICOLON){
							System.out.print("<semicolon />");
							getNextToken();
						}
					}
				}
			}
		}
		if (nextToken == Token.VARIABLE){
			getNextToken();
			if (ident()){
				System.out.print("</ident>");
				while (nextToken == Token.COMMA){
					System.out.print("</comma>");
					getNextToken();
					if (ident()){
						continue;
					}
				}
				if (nextToken == Token.SEMICOLON){
					System.out.print("</semicolon>");
					getNextToken();
				}				
			}
		}
		while (nextToken == Token.PROCEDURE){
			System.out.print("<procedure>");
			getNextToken();
			if (ident()) {
				System.out.print("<ident>");
				if (nextToken == Token.SEMICOLON){
					System.out.print("<semicolon />");
					getNextToken();
					if (block()) {
						continue;
					}
				}
			}
		}
		if (statement()) {
			System.out.print("</stmt>");
			return true;
		}
		else {
			System.out.println("Statement syntax error!\t"+nextStr);
			return false;
		}
	}

	public static boolean program(){
		if (block()){
			System.out.print("</block>");
			if (nextToken == Token.END_OF_INPUT) return true;
			else {
				System.out.println("Missing end of input!token="+nextToken);
				return false;
			}
		}
		else return false;
	}
	
	public static void main(String[] args){
		Parser p = new Parser();
	}
}