package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Constraints {
	public HashMap<String, HashSet<String>> complementary_set;
	public ArrayList<Variable> vars;
	public ArrayList<Entry<String,String>> pos_constraints, neg_constraints;
	
	public Constraints(ArrayList<Variable> vars, ArrayList<Entry<String,String>> pos_constraints, ArrayList<Entry<String,String>> neg_constraints){
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
		for(Entry<?, ?> constraint : pos_constraints)
			if( assignment.contains((String)constraint.getKey()) && !assignment.contains((String)constraint.getValue()))
				return false;
		for(Entry<?, ?> constraint : neg_constraints)
			if( assignment.contains((String)constraint.getKey()) && assignment.contains((String)constraint.getValue()))
				return false;
		return true;
	}
	
}
