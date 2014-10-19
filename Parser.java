/*
*Developer: Lior Shahverdi
* Description: This is a recursive descent parser which parses phrases in a grammar for a formal programming language 
* to determine if the given input is a syntactically valid collection of sentences in the language. 
* This parser will parse the file provided as input token by token (delimited by spaces " "), map each
* token to a terminal symbol in the grammar and move on to the next token. If the token does not map to a 
* terminal or gets recognized as a user-defined-name or number, the parser will terminate the parsing
* process at the incorrect portion of the input.
*/
import java.util.*;
import java.io.*;

public class Parser{
	private static Scanner file = null;
	private static String nextStr;
	private static Token nextToken;
	private static String[] lexemeArray;
	private static int lexArrLen;
	private static int lexemeIndex;

	private static ArrayStack sas; 
	private static HashMap listOfVars;

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
			init();
			getNextToken(); //initialize nextToken to the first token in the input
			commenceParsing(); //start the parsing process
			System.out.print("\nScan again? (y/n)"); //option to scan multiple files
			String nextChar = inputScan.next().substring(0,1).toUpperCase();
			if (nextChar.equals("N")) continueScan = false;
			else if (nextChar.equals("Y")) continue;
		}
	}

	private enum Token{
		//terminal symbols
		CONSTANT("const"), VARIABLE("var"), PROCEDURE("procedure"), CALL("call"), BEGIN("begin"), END("end"),
		IF("if"), THEN("then"), ELSE("else"), WHILE("while"), DO("do"), OPENPAREN("("), CLOSEPAREN(")"),
 		COMMA(","), SEMICOLON(";"), ASSIGN_EQUAL(":="),
 		EQUAL("="), NOT_EQUAL("!="), LESS_THAN("<"), GREATER_THAN(">"), LESS_THAN_EQUAL_TO("<="), GREATER_THAN_EQUAL_TO(">="),
		PLUS("+"), MINUS("-"), MULTIPLY("*"), DIVIDE("/"),
		NUMBER("0000"), USER_DEFINED_NAME("---"), END_OF_INPUT("$");
		
		private String str;

		private Token(String s){
			this.str = s;
		}

		//String representation of enum type
		private String getStr(){
    		return str;
		}

		/*
		*	The getEnum method consumes a String value and maps the corresponding enum type to that string value. If no such 
		*	string matches, the method returns null and therefore knows the input contains at least 1 invalid token.
		*/
		private static Token getEnum(String str) {
			for (Token t: Token.values()){
				if (str.equals(t.getStr())) return t;
			}
			return null;
		}
	}

	private static void init(){
		lexemeIndex = 0;
		lexemeArray = file.nextLine().split(" ");
		lexArrLen = lexemeArray.length;

		sas = new ArrayStack(100);
		listOfVars = new HashMap();
	}

	/*
	*	The commenceParsing method calls the start symbol in our grammar (top-down). 
	*/
	private static void commenceParsing(){
		if (program()){
			System.out.println("\nProgram is Syntactically Correct!");
		}
		else {
			System.out.println("Syntax Error!\tnextStr = "+nextStr);
			System.exit(0);
		}
	}

	/*
	*	The getNextToken method sets the value of nextToken to the nextToken in the input.
	*	If the string in the input is not mapped to a terminaly symbol in the getEnum method, or is 
	*	selected by the regular expression for user defined names or number, then our string value 
	*	is null and therefore is an invalid token. 
	*/
	private static void getNextToken(){
		if (lexemeIndex <= (lexArrLen-1)) {
			nextStr = lexemeArray[lexemeIndex];
			if (lexemeIndex < (lexArrLen-1)) lexemeIndex++;
			else if (lexemeIndex == (lexArrLen-1)){
				lexemeIndex = 0;
				if (file.hasNextLine()){
					lexemeArray = file.nextLine().split(" ");
					lexArrLen = lexemeArray.length;
				}
			}
			nextToken = null;
			nextToken = Token.getEnum(nextStr);
			if (nextToken == null){
				if (nextStr.matches("[a-zA-Z]{1}[\\w]*")){
					nextToken = Token.USER_DEFINED_NAME;
				}
				if (nextStr.matches("[0-9]+")){
					nextToken = Token.NUMBER;
				}
				if (nextToken == null) {
					System.out.println("INVALID ==>"+nextStr);
					System.exit(0);
				}
			}
		}
		else {
			System.out.println("Array out of bounds exception!\t lexemeIndex = "+lexemeIndex);
			System.exit(0);
		}
	}

	private static void pushIdentValue() {
		try{
			int varVal = (int) listOfVars.get(nextStr);
			System.out.println("vv = "+varVal);
		} catch (NullPointerException e){System.out.println("No value assigned to this identifier!\n"+e);}		
	}

	// <factor> := ident | number | '(' expression ')'
	private static boolean factor() {
		if (nextToken == Token.USER_DEFINED_NAME) {
			getNextToken();
			System.out.print("<ident>");
			return true;
		}
		else if (nextToken == Token.NUMBER) {
			getNextToken();
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

	// <term> := factor { '*' | '/' } factor
	private static boolean term() {
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

	// <expression> := ['+' | '-'] term { '+' | '-' } term  
	private static boolean expression() {
		System.out.print("<expression />");
		if (nextToken == Token.PLUS){
			System.out.print("<+>");
			getNextToken();
		}
		else if (nextToken == Token.MINUS){
			System.out.print("<->");
			getNextToken();
		} 
		if (term()) {
			System.out.print("<term>");
			while (nextToken == Token.PLUS || nextToken == Token.MINUS){
				System.out.print("<+|->");
				getNextToken();
				if (term()){
					System.out.print("<term>");
					continue;
				}
				else return false;
			}
			return true;
		}
		else return false;
	}

	// <relOp> := '=' | '!=' | '<' | '>' | '<=' | '>='
	private static boolean relOp(){
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

	// <condition> := expression relOp expression
	private static boolean condition() {
		if (expression()){
			System.out.println("\n<condition />");
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
	
	// <iterativeStat> := 'while' condition 'do' statement
	private static boolean iterativeStat() {
		if (nextToken == Token.WHILE){
			System.out.println("<iterativeStmt />");
			System.out.print("<while>");
			getNextToken();
			if (condition()) {
				System.out.println("</condition>");
				if (nextToken == Token.DO){
					System.out.print("<do>");
					getNextToken();
					if (statement()){
						System.out.print("</stmt>");
						return true;
					}
					else {
						System.out.print(" Improper statement! \tnt = "+nextToken);
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

	// <selectionStat> := 'if' condition 'then' statement 'else' statement
	private static boolean selectionStat() {
		if (nextToken == Token.IF){
			System.out.print("<selectionStmt />\n");
			System.out.print("<if>");
			getNextToken();
			if (condition()) {
				System.out.print("</condition>");
				if (nextToken == Token.THEN) {
					System.out.print("<then>");
					getNextToken();
					if (statement()) {
						System.out.print("\n</stmt>");
						if (nextToken == Token.ELSE) {
							System.out.print("\n<else>");
							getNextToken();
							if (statement()){
								System.out.print("</stmt>");
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
	
	// <compoundStat> := 'begin' statement { ; statement } 'end' 
	private static boolean compoundStat() { 
		if (nextToken == Token.BEGIN) {
			System.out.print("<compoundStmt />");
			System.out.print("\n<begin>");
			getNextToken();
			if (statement()) {
				while(nextToken == Token.SEMICOLON){
					System.out.print("<semicolon>");
					getNextToken();
					if (statement()){
						System.out.print("\n</stmt>");
						continue;
					}
					else return false;
				}
				if (nextToken == Token.END){
					System.out.print("\n<end>");
					getNextToken();
					return true;
				}
				else return false;
			}
			else return false;
		}
		else return false; 
	}
	
	// <procedureCallStat> := 'call' ident
	private static boolean procedureCallStat(){
		if (nextToken == Token.CALL){
			System.out.print("<procedureCallStmt />");
			System.out.print("<call>");
			getNextToken();
			if (nextToken == Token.USER_DEFINED_NAME) {
				pushIdentValue();
				getNextToken();
				System.out.print("<ident>");
				return true;
			}
			else return false;
		}
		else {
			System.out.println("No procedure call today\tnt = "+nextToken);
			return false;
		}
	}

	// <assignmentStat> := ident ':=' expression 
	private static boolean assignmentStat(){
		if (nextToken == Token.USER_DEFINED_NAME){
			getNextToken();
			System.out.print("<asgnStmt />");
			System.out.print("\n<ident>");
			if (nextToken == Token.ASSIGN_EQUAL){
				System.out.print("<:=>");
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

	// <statement> := assignmentStat | procedureCallStat | compoundStat | selectionStat | iterativeStat 
	private static boolean statement() {
		System.out.println("\n<stmt />");
		if (assignmentStat()) { 
			System.out.print("\n</asgnStmt>");
			return true; }
		else if (procedureCallStat()) { 
			System.out.print("\n</procedureCallStmt>");
			return true; }
		else if (compoundStat()) { 
			System.out.print("\n</compoundStmt>");
			return true; }
		else if (selectionStat()) { 
			System.out.print("\n</selectionStmt>");
			return true; }
		else if (iterativeStat()) { 
			System.out.print("\n</iterativeStmt>");
			return true; }
		else 
			{
				System.out.print("No statement today :((");
				return false;
			}
	}

	/*		   [ 'const' ident = number { , ident = number } ; ]
	*	   	   [ 'var' ident { , ident } ; ]
	*	   	   { 'procedure' ident ; block }
	*	   	   statement                                             */
	private static boolean block(){
		System.out.print("\n<block />");
		if (nextToken == Token.CONSTANT) {
			System.out.print("\n<constant>");
			getNextToken();
			if (nextToken == Token.USER_DEFINED_NAME) {
				getNextToken();
				System.out.print("<ident>");
				if (nextToken == Token.EQUAL){
					System.out.print("<equal>");
					getNextToken();
					if (nextToken == Token.NUMBER){
						getNextToken();
						System.out.print("<number>");
						while (nextToken == Token.COMMA){
							System.out.print("<comma>");
							getNextToken();
							if (nextToken == Token.USER_DEFINED_NAME){
								getNextToken();
								System.out.print("<ident>");
								if (nextToken == Token.EQUAL){
									System.out.print("<equal>");
									getNextToken();
									if (nextToken == Token.NUMBER){
										getNextToken();
										System.out.print("<number>");
										continue;
									}
									else return false;
								}
								else return false;	
							}
							else return false;
						}
						if (nextToken == Token.SEMICOLON){
							System.out.print("<semicolon>");
							getNextToken();
						}
						else return false;
					}
					else return false;
				}
				else return false;
			}
			else return false;
		}
		if (nextToken == Token.VARIABLE){
			System.out.print("<variable>");
			getNextToken();
			if (nextToken == Token.USER_DEFINED_NAME){
				getNextToken();
				System.out.print("<ident>");
				while (nextToken == Token.COMMA){
					System.out.print("<comma>");
					getNextToken();
					if (nextToken == Token.USER_DEFINED_NAME){
						getNextToken();
						System.out.print("<ident>");
						continue;
					}
				}
				if (nextToken == Token.SEMICOLON){
					System.out.print("<semicolon>");
					getNextToken();
				}		
				else return false;		
			}
			else return false;
		}
		while (nextToken == Token.PROCEDURE){
			System.out.print("\n<procedure>");
			getNextToken();
			if (nextToken == Token.USER_DEFINED_NAME) {
				getNextToken();
				System.out.print("<ident>");
				if (nextToken == Token.SEMICOLON){
					System.out.print("<semicolon>");
					getNextToken();
					if (block()) {
						System.out.print("\n</block>");
						continue;
					}
					else return false;
				}
				else return false;
			}
			else return false;
		}
		if (statement()) {
			System.out.print("\n</stmt>");
			return true;
		}
		else {
			System.out.println("Statement syntax error!\t"+nextStr);
			return false;
		}
	}

	// <program> := block $
	private static boolean program(){
		if (block()){
			System.out.print("\n</block>");
			if (nextToken == Token.END_OF_INPUT) return true;
			else {
				System.out.println("Missing end of input! token="+nextToken);
				return false;
			}
		}
		else return false;
	}
	
	public static void main(String[] args){
		Parser p = new Parser();
	}
}