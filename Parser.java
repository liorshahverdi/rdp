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
			String nextInput = inputScan.next();
			if (nextInput.toUpperCase().equals("N")) continueScan = false; 
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
			System.out.println("Syntactically Correct Program Complete!");
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
		System.out.println("nt is now ==>"+nextToken );
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

		System.out.println("??\tnt = "+nextToken);
		if (term()) {
			while (nextToken == Token.PLUS || nextToken == Token.MINUS){
				getNextToken();
				if (term()){
					System.out.println("oh woww");
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
		System.out.println("Condition reached! nt = "+nextToken);
		if (expression()){
			System.out.println("Expression true! nt ="+nextToken);
			if (relOp()) {
				if (expression()){
					System.out.println("Expression true! nt ="+nextToken);
					return true;
				}
				else return false;
			}
			else {
				System.out.println("relOp is TOTALLY NOT true! nt ="+nextToken+"\tstr = "+nextStr);
				return false;
			}
		}
		else return false;
	}
	
	public static boolean iterativeStat() {
		System.out.println("And so they entered into the iterative.. nt = "+nextToken);
		if (nextToken == Token.WHILE){
			getNextToken();
			System.out.println("nt = "+nextToken+" while down");
			if (condition()) {
				System.out.println("Condition down nt = "+nextToken);
				if (nextToken == Token.DO){
					getNextToken();
					if (statement()){
						return true;
					}
					else {
						System.out.println("nt = "+nextToken);
						return false;
					}
				
				}
				else return false;
			}
			else return false;
		}
		else return false;
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
				System.out.println("Landing? nt = "+nextToken);
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
		System.out.println("procedureCallStat reached! mytoken ->"+nextToken);
		if (nextToken == Token.CALL){
			System.out.println ("Commencing call!");
			getNextToken();
			if (ident()) {
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
		System.out.println("assignmentStat started!\tnextStr = "+nextStr+"\tnt = "+nextToken);
		if (ident()){
			System.out.println("ident condition for assignment Stmt passed! str = "+nextStr);
			if (nextToken == Token.ASSIGN_EQUAL){
				System.out.println("nt =>"+nextToken);
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
		//need to find a way for getNextToken to be in proper position
		//for proper branching
		if (assignmentStat()) { 
			System.out.println("Assignment Statement indeed!");
			return true; }
		else if (procedureCallStat()) { 
			System.out.println("Procedure Call Statement Indeed!");
			return true; }
		else if (compoundStat()) { 
			System.out.println("Compound Statement Indeed!");
			return true; }
		else if (selectionStat()) { 
			System.out.println("Selection Statement Indeed!");
			return true; }
		else if (iterativeStat()) { 
			System.out.println("Iterative Statement Indeed!");
			return true; }

		else 
			{
				System.out.println("No statement today :((\t-->"+nextStr);
				return false;
			}
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
		System.out.println("nt-> "+nextToken+"\t str-> "+nextStr);
		if (statement()) {
			System.out.println("Statement went through! \tnextStr = "+nextStr);
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