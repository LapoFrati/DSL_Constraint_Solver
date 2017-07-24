package solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import parsing.ParseError;
import parsing.Parser;


public class Test2{
        public static void main(String args[]) throws IOException, ParseError
        {
        	
        	//Parser parser = new Parser("src/parsing/test.txt");
        	Parser parser = new Parser("src/parsing/sudoku.txt");
    		DSL dsl = parser.parse();
        	dsl.print();
        	Solver solver = new Solver(new LinkedList<>(dsl.vars), dsl.constraints);
        
        	ArrayList<Assignment> solutions = new ArrayList<>();
        	for(Assignment ass : solver){
        		solutions.add(ass);
        	}
        	
        	
        	System.out.println("Solutions found: " + solutions.size());
        	for(Assignment assign : solutions)
        		assign.printSudoku();
        	;
        }
}