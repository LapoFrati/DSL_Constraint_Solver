package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Constraints {
	public HashMap<String, HashSet<String>> complementary_set;
	public ArrayList<Variable> vars;
	public HashMap<String,HashSet<String>> pos_constraints, neg_constraints;
	
	public Constraints(ArrayList<Variable> vars, HashMap<String,HashSet<String>> pos_constraints, HashMap<String,HashSet<String>> neg_constraints){
		this.pos_constraints = pos_constraints;
		this.neg_constraints = neg_constraints;
		this.vars = vars;
		this.complementary_set = new HashMap<>();
		
		for (Variable var : vars){
			for (String value : var.domain){
				HashSet<String> temp = (HashSet<String>) var.domain.stream().filter(el -> !el.equals(value)).collect(Collectors.toSet());
				complementary_set.put(value, temp);
			}
		}
	}
	
	public HashSet<String> getComplementarySet(String key){
		return complementary_set.get(key);
	}
	
	public boolean check_consistency(Assignment assignment){
		for (String val :assignment.assignment){
			if(pos_constraints.containsKey(val) && !assignment.containsAll(pos_constraints.get(val)))
				return false;
			if(neg_constraints.containsKey(val) &&  assignment.containsAll(neg_constraints.get(val)))
				return false;
				
		}
		return true;
	}
	
}
