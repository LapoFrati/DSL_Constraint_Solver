package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Assignment {
	ArrayList<String> assignment;
	public HashMap<String,HashSet<String>> active_pos_constraints, active_neg_constraints;
	int size, curr_size;
	
	public Assignment(int size){
		this.size = size;
		this.assignment = new ArrayList<>();
		this.active_pos_constraints = new HashMap<>();
		curr_size = 0;
	}
	
	public Assignment(int size, ArrayList<String> assignment, int curr_size){
		this.size = size;
		this.assignment = assignment;
		this.active_pos_constraints = new HashMap<>();
		this.active_neg_constraints = new HashMap<>();
		this.curr_size = curr_size;
	}
	
	public void assign(String value){
		assignment.add(value);
		curr_size++;
	}
	
	public ArrayList<String> getAssignment(){
		return assignment;
	}
	
	public boolean complete(){
		return curr_size == size;
	}
	
	public boolean contains(String value){
		return assignment.contains(value);
	}
	
	public boolean containsAll(Set<String> constraints){
		return assignment.containsAll(constraints);
	}
	
	@SuppressWarnings("unchecked")
	public Assignment clone(){
		return new Assignment(size, (ArrayList<String>) assignment.clone(), curr_size);
	}
	
	public void print(){
		System.out.println(Arrays.toString(assignment.toArray()));
	}
	
	public void addExplanationPositive(String val, HashSet<String> pos_constraints){
		HashSet<String> values = active_pos_constraints.get(val);
		if(values == null)
			active_pos_constraints.put(val, pos_constraints);
		else
			values.addAll(pos_constraints);
	}
	
	public void addExplanationNegative(String val, String neg_constraint){
		HashSet<String> values = active_neg_constraints.get(val);
		if(values == null)
			active_neg_constraints.put(val, new HashSet<>(Arrays.asList(neg_constraint)));
		else
			values.add(neg_constraint);
	}
	
	public void explain(){
		for(String val : assignment){
			System.out.print(val + " -> ");
			if(active_pos_constraints.containsKey(val))
				System.out.print(active_pos_constraints.get(val) + ", ");
			else
				System.out.print("No pos constraints, ");
			
			if(active_neg_constraints.containsKey(val))
				System.out.println("!"+active_neg_constraints.get(val));
			else
				System.out.println("No neg constraints");
		}
	}
	
	public void printSudoku(){
		for(int i = 0; i < 16; i++){
			System.out.print( assignment.get(i).charAt(1) + " " );
			if((i+1) % 4 == 0)
				System.out.println();
		}
		System.out.println();
	}
}
