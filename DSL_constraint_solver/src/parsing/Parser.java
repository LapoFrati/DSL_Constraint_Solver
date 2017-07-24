package parsing;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import parsing.Token.TokenType;
import solver.Constraints;
import solver.DSL;
import solver.Variable;


public class Parser {
	
	private String path;
	private Queue<Token> tokens;

	public Parser(String input_file) {
		path = input_file;
	}
	
	public DSL parse() throws IOException, ParseError {
		Lexer lexer = new Lexer(path);
		tokens = lexer.lex();
		return dsl();
	}
	
	public DSL dsl() throws ParseError{
		LinkedList<Variable> vars = vars();
		HashMap<String,HashSet<String>> pos_constraints = constr();
		HashMap<String,HashSet<String>> neg_constraints = constr();
		Constraints constraints = new Constraints(vars, pos_constraints, neg_constraints);
		DSL dsl = new DSL(vars, constraints);
		return dsl;
	}
	
	public HashMap<String,HashSet<String>> constr() throws ParseError{ // { (y1, z1), (y2, z2), (y3, z3) }\n
		HashMap<String,HashSet<String>> constraints = new HashMap<>();
		Token val1,val2;
		optional(TokenType.NOT);
		expect(TokenType.CURLYOPEN);
		while(!tokens.peek().is(TokenType.CURLYCLOSE)){
			expect(TokenType.PARENOPEN);
			val1 = expect(TokenType.VALUE);
			expect(TokenType.COMMA);
			val2 = expect(TokenType.VALUE);
			expect(TokenType.PARENCLOSE);
			optional(TokenType.COMMA);
			if(constraints.containsKey(val1.get()))
				constraints.get(val1.get()).add(val2.get());
			else{
				HashSet<String> new_entry = new HashSet<String>();
				new_entry.add(val2.get());
				constraints.put(val1.get(), new_entry);
			}	
		}
		expect(TokenType.CURLYCLOSE);
		optional(TokenType.NEWLINE);
		return constraints;
	}
	
	public LinkedList<Variable> vars() throws ParseError{
		LinkedList<Variable> vars = new LinkedList<>();
		while(!tokens.peek().is(TokenType.CURLYOPEN))
			vars.add(var());
		return vars;
	}
	
	public Variable var() throws ParseError{ // x = { x1, x2, x3 }
		Token name = expect(TokenType.VAR);
		expect(TokenType.EQUAL);
		LinkedList<String> values = val_list();
		return new Variable(name.get(), values);
	}
	
	public LinkedList<String> val_list() throws ParseError{ // { x1, x2, x3 }\n
		Token curr_tok;
		LinkedList<String> values = new LinkedList<>();
		curr_tok = expect(TokenType.CURLYOPEN);
		while(!tokens.peek().is(TokenType.CURLYCLOSE)){
			curr_tok = expect(TokenType.VALUE);
			values.add(curr_tok.get());
			optional(TokenType.COMMA);
		}
		expect(TokenType.CURLYCLOSE);
		optional(TokenType.NEWLINE);
		return values;
	}
	
	// removes the first token, checks it against the pattern and returns it
	public Token expect(TokenType type) throws ParseError{
		Token curr_tok = null;
		if(!tokens.isEmpty()) curr_tok = tokens.remove();
			if(!curr_tok.is(type)) 
				throw new ParseError("Expected "+type+" but got "+curr_tok + ".");
		return curr_tok;
	}
	
	public void optional(TokenType type){
		if(!tokens.isEmpty() && tokens.peek().is(type)) tokens.remove();
	}
}
