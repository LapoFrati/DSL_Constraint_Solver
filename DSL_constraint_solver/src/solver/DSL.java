package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DSL {
	public ArrayList<Variable> vars;
	public HashMap<String,HashSet<String>> pos_constraints, neg_constraints;
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
		for (String val: pos_constraints.keySet())
            System.out.println(val + "  -> " + pos_constraints.get(val));
		for (String val: neg_constraints.keySet())
            System.out.println(val + "  !-> " + neg_constraints.get(val));
	}
}
