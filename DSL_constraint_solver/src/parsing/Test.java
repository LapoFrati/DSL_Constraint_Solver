package parsing;

import java.io.IOException;
import solver.DSL;

public class Test {
	
	public static void main(String args[]) throws IOException, ParseError {
		
		Parser parser = new Parser("src/parsing/sudoku.txt");
		//Parser parser = new Parser("src/parsing/test.txt");
		DSL dsl = parser.parse();
		//dsl.solve_recursively().printSudoku();
		dsl.solve_recursively().explain();
		
		/*
		System.out.println();
		ArrayList<Variable> copy = new ArrayList<Variable>();
		for (Variable var: dsl.vars){
			copy.add(var.clone());
		}
		
		copy.get(0).domain.add("a");
		System.out.println("Original:\t"+dsl.vars.get(0).getDomain());
		System.out.println("Copy:\t\t" + copy.get(0).getDomain());
		
		ArrayList<String> test = new ArrayList<String>();
		test.add("test");
		ArrayList<String> test2 = (ArrayList<String>) test.clone();
		test2.add("test2");
		System.out.println(test);
		System.out.println(test2);
	*/
	}
}
