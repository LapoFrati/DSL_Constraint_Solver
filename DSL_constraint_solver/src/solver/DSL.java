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
	
	public void print(){
		for(Variable var : vars)
			var.print();
		constraints.print();
	}
}
