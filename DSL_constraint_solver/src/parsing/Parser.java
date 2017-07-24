package parsing;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
		ArrayList<Variable> vars = vars();
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
		while(!tokens.peek().type.equals(TokenType.CURLYCLOSE)){
			expect(TokenType.PARENOPEN);
			val1 = expect(TokenType.VALUE);
			expect(TokenType.COMMA);
			val2 = expect(TokenType.VALUE);
			expect(TokenType.PARENCLOSE);
			optional(TokenType.COMMA);
			if(constraints.containsKey(val1.data))
				constraints.get(val1.data).add(val2.data);
			else{
				HashSet<String> new_entry = new HashSet<String>();
				new_entry.add(val2.data);
				constraints.put(val1.data, new_entry);
			}	
		}
		expect(TokenType.CURLYCLOSE);
		optional(TokenType.NEWLINE);
		return constraints;
	}
	
	public ArrayList<Variable> vars() throws ParseError{
		ArrayList<Variable> vars = new ArrayList<>();
		while(!tokens.peek().type.equals(TokenType.CURLYOPEN))
			vars.add(var());
		return vars;
	}
	
	public Variable var() throws ParseError{ // x = { x1, x2, x3 }
		Token name = expect(TokenType.VAR);
		expect(TokenType.EQUAL);
		ArrayList<String> values = val_list();
		return new Variable(name.data, values);
	}
	
	public String var_name() throws ParseError{
		return expect(TokenType.VAR).data;
	}
	
	public ArrayList<String> val_list() throws ParseError{ // { x1, x2, x3 }\n
		Token curr_tok;
		ArrayList<String> values = new ArrayList<>();
		curr_tok = expect(TokenType.CURLYOPEN);
		while(!tokens.peek().type.equals(TokenType.CURLYCLOSE)){
			curr_tok = expect(TokenType.VALUE);
			values.add(curr_tok.data);
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
			if(!curr_tok.type.equals(type)) 
				throw new ParseError("Expected "+type+" but got "+curr_tok + ".");
		return curr_tok;
	}
	
	public void optional(TokenType type){
		if(!tokens.isEmpty() && tokens.peek().type.equals(type)) tokens.remove();
	}
}
