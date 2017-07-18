package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.NoSuchElementException;


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
			}
			
			System.out.print("assign: "); assignment.print();
			System.out.println("invalid: "+invalid_values);
			if(!assignment.complete()){
				Variable curr_var = remaining_vars.pop(); // get a variable from the list of available ones
				
				for (String value : curr_var.domain){ // for each value in the domain of that variable try to assign it
					Assignment new_assignment = assignment.clone();
					new_assignment.assign(value);
					// now compute the effect of the assignment on positive/negative constraints
					@SuppressWarnings("unchecked")
					HashSet<String> new_invalid_values = (HashSet<String>) invalid_values.clone();
					for(Entry<?,?> constraint : constraints.pos_constraints){ 
						if ( value == (String) constraint.getKey()){ // for positive constraints mark all the other values as invalid
							new_invalid_values.addAll(constraints.getComplementarySet((String)constraint.getValue()));
						}
					}
					for(Entry<?,?> constraint : constraints.neg_constraints){
						if ( value.equals((String) constraint.getKey())){
							new_invalid_values.add((String)constraint.getValue());
						}
					}
					
					LinkedList<Variable> new_remaining_vars = new LinkedList<>();
					for(Variable var : remaining_vars){
						new_remaining_vars.add(var.clone());
					}
					
					children.add(new SearchNode(this, new_invalid_values, new_assignment, new_remaining_vars));
				}
			}
			
		}
		
		
	}
}
