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
		for (String val :assignment.getAssignment()){
			if(pos_constraints.containsKey(val))
				if(!assignment.containsAll(pos_constraints.get(val)))
					return false;
				else
					assignment.addExplanationPositive(val, pos_constraints.get(val));
				
			if(neg_constraints.containsKey(val))
				for ( String str : neg_constraints.get(val))
					if( assignment.contains(str))
						return false;
					else
						assignment.addExplanationNegative(val, str);
		}
		return true;
	}
	
	public void print(){
		for (String val: pos_constraints.keySet())
            System.out.println(val + "  -> " + pos_constraints.get(val));
		for (String val: neg_constraints.keySet())
            System.out.println(val + "  !-> " + neg_constraints.get(val));
	
	}
	
}
