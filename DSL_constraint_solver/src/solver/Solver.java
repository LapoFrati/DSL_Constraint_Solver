package solver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
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
		System.out.println("starting iterator");
		producer = new Thread(new Producer(), "producer");
		producer.setDaemon(true);
		producer.start();
		
		return new Consumer();
	}
	
	public class Consumer implements Iterator<Assignment>{

		@Override
		public boolean hasNext() {
			System.out.println("hasNext");
			return waitNext();
		}

		@Override
		public Assignment next() {
			lock.lock();
			try{
				nextAvailable = false;
				System.out.println("next"+next.size);
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
			for(int i = 0; i<5; i++){
				try {
					System.out.println("yelding"+i);
					yield(new Assignment(i));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
				while(!nextAvailable)
					nextRequested.await();
			} finally { lock.unlock(); }
		}
	}
}
