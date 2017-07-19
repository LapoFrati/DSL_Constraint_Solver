package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;


public class SearchTree {
	public LinkedList<Variable> vars;
	public Constraints constraints;
	public Producer producer;
	public ArrayBlockingQueue<Assignment> queue;
	public boolean hasNext;
	public Object lock;
	
	public SearchTree(ArrayList<Variable> vars , Constraints constraints) {
		this.vars = new LinkedList<>(vars);
		this.constraints = constraints;
		this.queue = new ArrayBlockingQueue<>(1);
		this.hasNext = true;
		this.lock = new Object();
		this.producer = new Producer();
		new Thread(producer).start();
	}
	
	public void finishedSearch(){
		synchronized (lock) {
			hasNext = false;
		}
	}
	
	public boolean hasNext(){
		synchronized (lock) {
				return !queue.isEmpty() || hasNext; 
		}
	}
	
	public Assignment next(){
		try { 	return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public class Producer implements Runnable{
		public void run(){
			explore(new Assignment(vars.size()), vars);
			finishedSearch();
		}
		
		public void explore(Assignment assignment, LinkedList<Variable> remaining_vars){
			if(!assignment.complete()){
				Variable curr_var = remaining_vars.pop(); // get a variable from the list of available ones
				for (String value : curr_var.domain){ // for each value in the domain of that variable try to assign it
					Assignment new_assignment = assignment.clone();
					new_assignment.assign(value);
					HashSet<String> new_invalid_values = update_invalid_values(value);
					LinkedList<Variable> new_remaining_vars = update_remaining_variables(remaining_vars, new_invalid_values);
					explore(new_assignment, new_remaining_vars);
				}
			} else {
				try{
					queue.put(assignment);
					System.out.println("Produced "+assignment.getAssignment());
            	} catch (InterruptedException e) {
                e.printStackTrace();
            	}
            }
		}
		
		public LinkedList<Variable> update_remaining_variables(LinkedList<Variable> remaining_vars, HashSet<String> new_invalid_values){
			LinkedList<Variable> new_remaining_vars = new LinkedList<>();
			for(Variable var : remaining_vars){
				Variable new_var = var.clone();
				new_var.domain.removeAll(new_invalid_values);
				new_remaining_vars.add(new_var);
			}
			return new_remaining_vars;
		}
		
		public HashSet<String> update_invalid_values(String value){
			HashSet<String> new_invalid_values = new HashSet<>();
			if ( constraints.pos_constraints.containsKey(value))
				for(String constraint : constraints.pos_constraints.get(value)){
					new_invalid_values.addAll(constraints.getComplementarySet(constraint));
				}
			if ( constraints.neg_constraints.containsKey(value))
				for(String constraint : constraints.neg_constraints.get(value)){
					new_invalid_values.add(constraint);
				}
			return new_invalid_values;
		}
	}
}
