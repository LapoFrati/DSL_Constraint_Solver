package solver;

import java.util.ArrayList;
import java.util.Map.Entry;

public class DSL {
	public ArrayList<Variable> vars;
	public ArrayList<Entry<String,String>> pos_constraints, neg_constraints;
	public Assignment solution;
	public Constraints constraints;
	
	public Assignment solve_recursively(){
		if(recurr(new Assignment(vars.size()),0) == false)
			System.out.println("Unsatisfiable;");
		return solution;
	}

	public boolean recurr(Assignment assignment, int pos){
		boolean found = false;
		
		if(assignment.complete()){
			if(constraints.check_consistency(assignment)){
				solution = assignment;
				found = true;
			}
		}
		else
			for( String value : vars.get(pos).getDomain()){
				Assignment new_assign = assignment.clone();
				new_assign.assign(value);
				if(found = recurr(new_assign, pos+1))
					break;
			}
		return found;
	}
	
	public void print(){
		for(Variable var : vars)
			var.print();
		for (Entry<String, String> val: pos_constraints)
            System.out.println(val.getKey() + "  -> " + val.getValue());
		for (Entry<String, String> val: neg_constraints)
            System.out.println(val.getKey() + " !-> " + val.getValue());
	}
}
