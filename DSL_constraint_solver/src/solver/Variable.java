package solver;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;

public class Variable {
	String var_name;
	public LinkedList<String> domain;
	
	public Variable(String name, LinkedList<String> values){
		var_name = name;
		domain = values;
	}
	
	public ArrayDeque<String> getDomain(){
		return new ArrayDeque<String>(domain);
	}
	
	public String getValue(int index){
		return domain.get(index);
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
