package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;


public class SearchTree {
	public LinkedList<Variable> vars;
	public Constraints constraints;
	public SearchNode root;
	
	
	public SearchTree(ArrayList<Variable> vars , Constraints constraints) {
		this.vars = new LinkedList<>(vars);
		this.constraints = constraints;
		this.root = new SearchNode(null, new HashSet<>(), new Assignment(vars.size()), this.vars);
	}
	
	public class SearchNode {
		Assignment assignment;
		SearchNode parent;
		HashSet<String> invalid_values;
		ArrayList<SearchNode> children;
		LinkedList<Variable> remaining_vars;
		
		public SearchNode(SearchNode parent, HashSet<String> invalid_values, Assignment assignment, LinkedList<Variable> vars){
			this.invalid_values = invalid_values;
			this.assignment = assignment;
			this.parent = parent;
			this.children = new ArrayList<>();
			//this.remaining_vars = vars;
			this.remaining_vars = new LinkedList<>();
			for(Variable var : vars){
				remaining_vars.add(var.clone());
			} //TODO: DO I NEED THIS?
			
			
			if(!assignment.complete()){
				Variable curr_var = remaining_vars.pop(); // get a variable from the list of available ones
				
				for (String value : curr_var.domain){ // for each value in the domain of that variable try to assign it
					Assignment new_assignment = assignment.clone();
					new_assignment.assign(value);
					// now compute the effect of the assignment on positive/negative constraints
					@SuppressWarnings("unchecked")
					HashSet<String> new_invalid_values = (HashSet<String>) invalid_values.clone();
					if ( constraints.pos_constraints.containsKey(value))
						for(String constraint : constraints.pos_constraints.get(value)){
							new_invalid_values.addAll(constraints.getComplementarySet(constraint));
						}
					if ( constraints.neg_constraints.containsKey(value))
						for(String constraint : constraints.neg_constraints.get(value)){
							new_invalid_values.add(constraint);
						}
					
					LinkedList<Variable> new_remaining_vars = new LinkedList<>();
					for(Variable var : remaining_vars){
						Variable new_var = var.clone();
						new_var.domain.removeAll(new_invalid_values);
						new_remaining_vars.add(new_var);
					}
					
					children.add(new SearchNode(this, new_invalid_values, new_assignment, new_remaining_vars));
				}
			} else {
					
					System.out.print("assign: "); assignment.print();
					System.out.println(constraints.check_consistency(assignment));
					System.out.println("invalid: "+invalid_values);
				
			}
			
		}
		
		
	}
}
