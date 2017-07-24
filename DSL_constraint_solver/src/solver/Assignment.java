package solver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public class Assignment {
	LinkedList<String> assignment;
	public HashMap<String,HashSet<String>> active_pos_constraints, active_neg_constraints;
	int size, curr_size;
	
	public Assignment(int size){
		this.size = size;
		this.assignment = new LinkedList<>();
		this.active_pos_constraints = new HashMap<>();
		this.active_neg_constraints = new HashMap<>();
		curr_size = 0;
	}
	
	public void assign(String value){
		assignment.add(value);
		curr_size++;
	}
	
	public LinkedList<String> getAssignment(){
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
		Assignment new_assign = new Assignment(this.size);
		new_assign.assignment = (LinkedList<String>) this.assignment.clone();
		new_assign.curr_size = this.curr_size;
		new_assign.active_neg_constraints = (HashMap<String, HashSet<String>>) this.active_neg_constraints.entrySet().stream()
	    .collect(Collectors.toMap(e -> e.getKey(), e -> new HashSet<String>(e.getValue())));
		new_assign.active_pos_constraints = (HashMap<String, HashSet<String>>) this.active_pos_constraints.entrySet().stream()
			    .collect(Collectors.toMap(e -> e.getKey(), e -> new HashSet<String>(e.getValue())));
		/*for (Entry<String, HashSet<String>> entry: this.active_neg_constraints.entrySet()){
			System.out.println(entry);
			new_assign.active_neg_constraints.put(entry.getKey(), (HashSet<String>) entry.getValue().clone());
		}*/
		return new_assign;
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
