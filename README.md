README for Parser.java
Developer: Lior Shahverdi

A parser for a generic programming language.
The grammar for this language is as follows:

program 		-> block $
block 			-> [ 'const' ident = number { , ident = number } ; ]
		   	   [ 'var' ident { , ident } ; ]
		   	   { 'procedure' ident ; block }
		   	   statement
statement		-> assignmentStat | procedureCallStat | compoundStat | selectionStat | iterative Stat
assignmentStat		-> ident := expression
procedureCallStat	-> 'call' ident
compoundStat		-> 'begin' statement { ; statement } 'end'
selectionStat		-> 'if' condition 'then' statement 'else' statement
iterativeStat		-> 'while' condition 'do' statement
condition 		-> expression relOp expression
relOp			-> = | != | < | > | <= | >= 
expression		-> [+|-] term { (+\-) term }
term			-> factor {(*|/) factor } 
factor			-> ident | number | '(' expression ')'
ident			-> letter { ( letter | digit ) }
number			-> digit { digit }
digit			-> 0 | 1 | ... | 9
letter			-> A | ...| Z | a | ... | z

