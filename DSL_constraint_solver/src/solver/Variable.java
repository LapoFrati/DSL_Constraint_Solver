package solver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class Variable {
	String var_name;
	int domain_size;
	public ArrayList<String> domain;
	boolean assigned;
	
	public Variable(String name, ArrayList<String> values){
		var_name = name;
		domain = values;
		assigned = false;
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
		Variable new_var = new Variable(this.var_name, (ArrayList<String>) this.domain.clone());
		return new_var;
	}
	
	public void print(){
		System.out.println(var_name + " : " +Arrays.toString(domain.toArray()));
	}
}
