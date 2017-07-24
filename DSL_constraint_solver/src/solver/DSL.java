package solver;

import java.util.LinkedList;

public class DSL {
	public LinkedList<Variable> vars;
	public Assignment solution;
	public Constraints constraints;
	
	public DSL(LinkedList<Variable> vars,Constraints constraints){
		this.vars = vars;
		this.constraints = constraints;
	}
	
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
		constraints.print();
	}
}
