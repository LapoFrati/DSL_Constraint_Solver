package solver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Solver implements Iterable<Assignment>{
	public LinkedList<Variable> vars;
	public Constraints constraints;
	public Thread producer;
	public Assignment next;
	public boolean hasNext;
	
	// SYNC
	public final Lock lock;
	public volatile boolean nextAvailable;
	public final Condition nextAvailableOrHasFinished;
	public final Condition nextRequested;
	
	public Solver(LinkedList<Variable> vars , Constraints constraints){
		this.vars = vars;
		this.constraints = constraints;
		this.lock = new ReentrantLock();
		this.nextAvailableOrHasFinished = lock.newCondition();
		this.nextRequested = lock.newCondition();
		this.nextAvailable = false;
		this.hasNext = true;

	}

	@Override
	public Iterator<Assignment> iterator() {
		producer = new Thread(new Producer(), "producer");
		producer.setDaemon(true);
		producer.start();
		
		return new Consumer();
	}
	/*
	public void noise(){
		try {
			Thread.sleep((long) (Math.random()*100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	*/
	public class Consumer implements Iterator<Assignment>{

		@Override
		public boolean hasNext() {
			return waitNext();
		}

		@Override
		public Assignment next() {
			lock.lock();
			try{
				nextAvailable = false;
				return next;
			} finally {
				lock.unlock();
			}
		}
		
		public boolean waitNext() {
			if (nextAvailable)
				return true;
			if (!hasNext)
				return false;
			lock.lock();
			try{
				nextRequested.signal();
				nextAvailableOrHasFinished.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally { lock.unlock(); }
			
			return hasNext;
		}
		
	}
	
	public class Producer implements Runnable{

		@Override
		public void run() {
			explore(new Assignment(vars.size()), vars);
			hasNext = false;
			lock.lock();
			try{
				nextAvailableOrHasFinished.signal();
			} finally {
				lock.unlock();
			}
		}
		
		protected void yield(Assignment assignment) throws InterruptedException {
			lock.lock();
			try{
				next = assignment;
				nextAvailable = true;
				nextAvailableOrHasFinished.signal();
				while(nextAvailable)
					nextRequested.await();
			} finally { lock.unlock(); }
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
					yield(assignment);
					//System.out.println("Produced "+assignment.getAssignment());
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
