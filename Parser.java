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
		//Object val = listOfVars.get(nextStr);
		if (listOfVars.get(nextStr) instanceof Integer){
			try{
				int identVal = (int) listOfVars.get(nextStr);
				//System.out.println("idval = "+identVal);
				sas.push(identVal);
			} catch (NullPointerException e){System.out.println(e);}
		}
		else if (listOfVars.get(nextStr) instanceof String){
			//System.out.println("It's a string!");
		}		
	}

	private static void pushNumberTokenValue() {
		int tokVal = Integer.parseInt(nextStr);
		sas.push(tokVal);
	}

	// <factor> := ident | number | '(' expression ')'
	private static boolean factor() {
		if (nextToken == Token.USER_DEFINED_NAME) {
			//System.out.print("<ident>");
			pushIdentValue();
			getNextToken();
			return true;
		}
		else if (nextToken == Token.NUMBER) {
			//System.out.print("<ident>");
			pushNumberTokenValue();
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

	// <term> := factor {(*|/) factor } 
	private static boolean term() {
		boolean workaround = false;
		do {
			if (workaround){
				if (nextToken == Token.MULTIPLY){
					getNextToken();
					if (factor()) {
						int op1 = (int) sas.pop();
						int op2 = (int) sas.pop();
						sas.push(op1*op2);
						continue;
					}
					else return false;
				}
				else if (nextToken == Token.DIVIDE){
					getNextToken();
					if (factor()) {
						int op1 = (int) sas.pop();
						int op2 = (int) sas.pop();
						sas.push(op2/op1);
						continue;
					}
					else return false;
				}
			}
			if (factor()){
				workaround = true;
			}else return false;
		} while (nextToken == Token.MULTIPLY || nextToken == Token.DIVIDE);
		return true;
	}

	// <expression> := ['+' | '-'] term {('+'|'-') term} 
	private static boolean expression() {
		boolean firstTermIsNegative = false;
		boolean skipsFirstIteration = false;
		if (nextToken == Token.PLUS){
			getNextToken();
		}
		else if (nextToken == Token.MINUS){
			getNextToken();
			firstTermIsNegative = true;
		} 
		do {
			if (skipsFirstIteration){
				if (nextToken == Token.PLUS){
					getNextToken();
					if (term()){
						int op1 = (int) sas.pop();
						int op2 = (int) sas.pop();
						sas.push(op1+op2);
						continue;
					}
				}
				else if (nextToken == Token.MINUS){
					getNextToken();
					if (term()){
						int op1 = (int) sas.pop();
						int op2 = (int) sas.pop();
						sas.push(op2-op1);
						continue;
					}
				}
			}
			if (term() && !skipsFirstIteration) {
				if (firstTermIsNegative){
					int topOperand = (int) sas.pop();
					sas.push(-1 * topOperand);
					firstTermIsNegative = false;
				}
				skipsFirstIteration = true;
				continue;
			}else return false;
		} while (nextToken == Token.PLUS || nextToken == Token.MINUS);
		return true;
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
			//System.out.println("\n<condition />");
			//System.out.println("</expression>");
			if (relOp()) {
				//System.out.println("<relOp>");
				if (expression()){
					//System.out.println("</expression>");
					return true;
				}
				else return false;
			}
			else {
				System.out.println("relOp is NOT true! nt ="+nextToken+"\tstr = "+nextStr);
				return false;
			}
		}else return false;
	}
	
	// <iterativeStat> := 'while' condition 'do' statement
	private static boolean iterativeStat() {
		if (nextToken == Token.WHILE){
			//System.out.println("<iterativeStmt />");
			//System.out.print("<while>");
			getNextToken();
			if (condition()) {
				//System.out.println("</condition>");
				if (nextToken == Token.DO){
					//System.out.print("<do>");
					getNextToken();
					if (statement()){
						//System.out.print("</stmt>");
						return true;
					}
					else {
						System.out.print(" Improper statement! \tnt = "+nextToken);
						return false;
					}
				}else return false;
			}else return false;
		}
		else {
			System.out.println("no while token!");
			return false;
		}
	}

	// <selectionStat> := 'if' condition 'then' statement 'else' statement
	private static boolean selectionStat() {
		if (nextToken == Token.IF){
			//System.out.print("<selectionStmt />\n");
			//System.out.print("<if>");
			getNextToken();
			if (condition()) {
				//System.out.print("</condition>");
				if (nextToken == Token.THEN) {
					//System.out.print("<then>");
					getNextToken();
					if (statement()) {
						//System.out.print("\n</stmt>");
						if (nextToken == Token.ELSE) {
							//System.out.print("\n<else>");
							getNextToken();
							if (statement()){
								//System.out.print("</stmt>");
								return true;
							}else return false;
						}else return false;
					}else return false;
				}else return false;
			}else return false;
		}
		else {
			System.out.println("No if:( nt = "+nextToken);
			return false;
		}
	}
	
	// <compoundStat> := 'begin' statement { ; statement } 'end' 
	private static boolean compoundStat() {
		if (nextToken == Token.BEGIN){
			do{
				getNextToken();
				if (statement()){
					continue;
				}else return false;
			} while (nextToken == Token.SEMICOLON);
			if (nextToken == Token.END){
				getNextToken();
				return true;
			}else return false;
		}else return false;
	}
	
	// <procedureCallStat> := 'call' ident
	private static boolean procedureCallStat(){
		if (nextToken == Token.CALL){
			//System.out.print("<procedureCallStmt />");
			//System.out.print("<call>");
			getNextToken();
			if (nextToken == Token.USER_DEFINED_NAME) {
				//System.out.print("<ident>");
				pushIdentValue();
				getNextToken();
				return true;
			}
			else return false;
		}
		else {
			//System.out.println("No procedure call today\tnt = "+nextToken);
			return false;
		}
	}

	// <assignmentStat> := ident ':=' expression 
	private static boolean assignmentStat(){
		if (nextToken == Token.USER_DEFINED_NAME){
			//System.out.print("<asgnStmt />");
			//System.out.print("\n<ident>");
			String identifier = nextStr;
			listOfVars.put(identifier, 0);
			getNextToken();
			if (nextToken == Token.ASSIGN_EQUAL){
				//System.out.print("<:=>");
				getNextToken();
				if (expression()){
					//System.out.print("</expression>");
					int expressionRes = (int) sas.pop();
					listOfVars.put(identifier, expressionRes);
					System.out.println("Key-> "+identifier+"\t\tValue-> "+listOfVars.get(identifier).toString());
					System.out.println("End of asgnStmt");
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
		//System.out.println("\n<stmt />");
		if (assignmentStat()) { 
			//System.out.print("\n</asgnStmt>");
			return true; }
		else if (procedureCallStat()) { 
			//System.out.print("\n</procedureCallStmt>");
			return true; }
		else if (compoundStat()) { 
			//System.out.print("\n</compoundStmt>");
			return true; }
		else if (selectionStat()) { 
			//System.out.print("\n</selectionStmt>");
			return true; }
		else if (iterativeStat()) { 
			//System.out.print("\n</iterativeStmt>");
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
		//       [ 'const' ident = number { , ident = number } ; ]
		//System.out.print("\n<block />");
		if (nextToken == Token.CONSTANT) {
			//System.out.print("\n<constant>");
			do {
				getNextToken();
				if (nextToken == Token.USER_DEFINED_NAME) {
					//System.out.print("<ident>");
					String initIdent = nextStr;
					getNextToken();
					if (nextToken == Token.EQUAL){
						//System.out.print("<equal>");
						getNextToken();
						if (nextToken == Token.NUMBER){
							int myNumber = Integer.parseInt(nextStr);
							listOfVars.put(initIdent, myNumber);
							System.out.println("Key-> "+initIdent+"\t\tValue-> "+listOfVars.get(initIdent).toString());
							getNextToken();
							//System.out.print("<number>");
						}else return false;
					}else return false; 
				}else return false;
			} while (nextToken == Token.COMMA);			
			if (nextToken == Token.SEMICOLON){
				//System.out.print("<semicolon>");
				System.out.println("End of constants\n");
				getNextToken();
			}else return false;
		}
		//        [ 'var' ident { , ident } ; ]
		if (nextToken == Token.VARIABLE){
			//System.out.print("<variable>");
			do {
				getNextToken();
				if (nextToken == Token.USER_DEFINED_NAME){
					//System.out.print("<ident>");
					if (listOfVars.get(nextStr) == null){
						listOfVars.put(nextStr, "Not Initialized");
					}
					System.out.println("Key-> "+nextStr+"\t\tValue-> "+listOfVars.get(nextStr).toString());
					getNextToken();
				}
			} while (nextToken == Token.COMMA);	
			if (nextToken == Token.SEMICOLON){
				//System.out.print("<semicolon>");
				System.out.println("End of variables\n");
				getNextToken();
			}else return false;		
		}
		//           { 'procedure' ident ; block }
		//			 statement  
		while (nextToken == Token.PROCEDURE){
			//System.out.print("\n<procedure>");
			getNextToken();
			if (nextToken == Token.USER_DEFINED_NAME) {
				//System.out.print("<ident>");
				pushIdentValue();
				getNextToken();
				if (nextToken == Token.SEMICOLON){
					//System.out.print("<semicolon>");
					getNextToken();
					if (block()) {
						//System.out.print("\n</block>");
						continue;
					}
					else return false;
				}
				else return false;
			}
			else return false;
		}
		if (statement()) {
			//System.out.print("\n</stmt>");
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
			//System.out.print("\n</block>");
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