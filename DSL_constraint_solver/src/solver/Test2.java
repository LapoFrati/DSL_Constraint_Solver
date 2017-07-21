package solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import parsing.ParseError;
import parsing.Parser;


public class Test2{
	
        public static String transform(String str){
        	if(str == "ab")
        		return "a";
        	if(str == "bc")
        		return "b";
        	return "c";
        }
 
        public static void main(String args[]) throws IOException, ParseError
        {
        	/*
        	ArrayList<String> test = new ArrayList<>();
        	test.add("ab");
        	test.add("bc");
        	test.add("ab");
        	
        	test = (ArrayList<String>) test.stream().map(Test2::transform).collect(Collectors.toList());
        	System.out.println(test);
        	ArrayList<Character> test3 = (ArrayList) "abc".chars().mapToObj(i -> (char)i).collect(Collectors.toList());
        	System.out.println(test3);
        	*/
        	
        	Parser parser = new Parser("src/parsing/test.txt");
    		DSL dsl = parser.parse();
    		//dsl.solve_recursively().print();
        	//SearchTree tree = new SearchTree(dsl.vars, dsl.constraints);
        	Solver tree = new Solver(new LinkedList<>(dsl.vars), dsl.constraints);
        
        	ArrayList<Assignment> solutions = new ArrayList<>();
        	for(Assignment ass : tree){
        		//System.out.println(ass.getAssignment());
        		solutions.add(ass);
        	}
        	
        	
        	System.out.println("Solutions:");
        	for(Assignment assign : solutions)
        		assign.print();
        	System.out.println(solutions.size());
        }
}