package solver;

import java.io.IOException;
import java.util.function.IntSupplier;

import parsing.ParseError;
import parsing.Parser;


public class Test2{
        private static class SqSupplier implements IntSupplier
        {
                int i = 0;
 
                @Override
                public int getAsInt()
                {
                        i++;
                        return i;
                }
        }
        
        public static String transform(String str){
        	if(str == "ab")
        		return "a";
        	if(str == "bc")
        		return "b";
        	return "c";
        }
 
        public static void main(String args[]) throws IOException, ParseError
        {
        	// Iterator implemented as anonymous class.  This uses generics but doesn't need to.
        /*	for (int i: new Iterable<Integer>() {
        	    @Override
        	    public Iterator<Integer> iterator() {
        	        return new Iterator<Integer>() {
        	            int counter = 1;

        	            @Override
        	            public boolean hasNext() {
        	                return counter <= 10000000;
        	            }

        	            @Override
        	            public Integer next() {
        	                return counter++;
        	            }

        	            @Override
        	            public void remove() {
        	                throw new UnsupportedOperationException();
        	            }
        	        };
        	    }
        	}) {
        	    System.out.println(i);
        	}
        	*/
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
    		dsl.solve_recursively().print();
        	Constraints constr = new Constraints(dsl.vars, dsl.pos_constraints, dsl.neg_constraints);
        	System.out.println(constr.complementary_set);
        	SearchTree tree = new SearchTree(dsl.vars, constr);
        }
}