package parsing;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import solver.Constraints;
import solver.DSL;
import solver.Variable;


public class Parser {
	
	private String path;
	private String allowed_var_names = "[a-z]";
	private String allowed_var_values = "[a-z][0-9]";
	private Queue<String> tokens;

	public Parser(String input_file) {
		path = input_file;
	}
	
	public DSL parse() throws IOException, ParseError {
		tokens = Lexer.tokenize(path);
		return dsl();
	}
	
	public DSL dsl() throws ParseError{
		DSL dsl = new DSL();
		dsl.vars = vars();
		dsl.pos_constraints = constr();
		dsl.neg_constraints = constr();
		Constraints constraints = new Constraints(dsl.vars, dsl.pos_constraints, dsl.neg_constraints);
		dsl.constraints = constraints;
		dsl.print();
		return dsl;
	}
	
	public HashMap<String,HashSet<String>> constr() throws ParseError{ // { (y1, z1), (y2, z2), (y3, z3) }\n
		HashMap<String,HashSet<String>> constraints = new HashMap<>();
		String val1,val2;
		optional("!");
		expect("\\{");
		while(!tokens.peek().matches("\\}")){
			expect("\\(");
			val1 = expect(allowed_var_values);
			expect("\\,");
			val2 = expect(allowed_var_values);
			expect("\\)");
			optional(",");
			if(constraints.containsKey(val1))
				constraints.get(val1).add(val2);
			else{
				HashSet<String> new_entry = new HashSet<String>();
				new_entry.add(val2);
				constraints.put(val1, new_entry);
			}	
		}
		expect("}");
		optional("\n");
		return constraints;
	}
	
	public ArrayList<Variable> vars() throws ParseError{
		ArrayList<Variable> vars = new ArrayList<>();
		while(!tokens.peek().matches("\\{"))
			vars.add(var());
		return vars;
	}
	
	public Variable var() throws ParseError{ // x = { x1, x2, x3 }
		String name = expect(allowed_var_names);
		expect("=");
		ArrayList<String> values = val_list();
		return new Variable(name, values);
	}
	
	public Character var_name() throws ParseError{
		return expect(allowed_var_names).charAt(0);
	}
	
	public ArrayList<String> val_list() throws ParseError{ // { x1, x2, x3 }\n
		String curr_tok;
		ArrayList<String> values = new ArrayList<>();
		curr_tok = expect("\\{");
		while(!tokens.peek().matches("\\}")){
			curr_tok = expect(allowed_var_values);
			values.add(curr_tok);
			optional(",");
		}
		expect("}");
		optional("\n");
		return values;
	}
	
	// removes the first token, checks it against the pattern and returns it
	public String expect(String pattern) throws ParseError{
		String curr_tok = null;
		if(!tokens.isEmpty()) curr_tok = tokens.remove();
			if(!curr_tok.matches(pattern)) 
				throw new ParseError("Expected "+pattern+" but got "+curr_tok + ".");
		return curr_tok;
	}
	
	public void optional(String pattern){
		if(!tokens.isEmpty() && tokens.peek().matches(pattern)) tokens.remove();
	}
}
