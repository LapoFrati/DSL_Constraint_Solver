package parsing;

import java.io.IOException;

import solver.DSL;

public class Test {
	
	public static void main(String args[]) throws IOException, ParseError {
		
		//Parser parser = new Parser("src/parsing/sudoku.txt");
		/*Parser parser = new Parser("src/parsing/test.txt");
		DSL dsl = parser.parse();
		Assignment ass = dsl.solve_recursively();
		ass.print();
		ass.explain();
		*/
		
		Parser newparser = new Parser("src/parsing/test.txt");
		DSL dsl = newparser.parse();
		dsl.print();
		dsl.solve_recursively().explain();
		
	}
}
