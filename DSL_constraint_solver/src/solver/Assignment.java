package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Assignment {
	ArrayList<String> assignment;
	int size, curr_size;
	
	public Assignment(int size){
		this.size = size;
		this.assignment = new ArrayList<>();
		curr_size = 0;
	}
	
	public Assignment(int size, ArrayList<String> assignment, int curr_size){
		this.size = size;
		this.curr_size = curr_size;
		this.assignment = assignment;
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
}
