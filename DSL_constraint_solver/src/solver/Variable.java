package solver;

import java.util.Arrays;
import java.util.LinkedList;

public class Variable {
	private String var_name;
	public LinkedList<String> domain;
	
	public Variable(String name, LinkedList<String> values){
		var_name = name;
		domain = values;
	}
	
	public int getDomainSize(){
		return domain.size();
	}
	
	public Variable clone(){
		@SuppressWarnings("unchecked")
		Variable new_var = new Variable(this.var_name, (LinkedList<String>) this.domain.clone());
		return new_var;
	}
	
	public void print(){
		System.out.println(var_name + " : " +Arrays.toString(domain.toArray()));
	}
}
