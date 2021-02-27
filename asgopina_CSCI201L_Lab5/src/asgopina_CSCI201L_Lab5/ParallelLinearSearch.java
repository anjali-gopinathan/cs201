package asgopina_CSCI201L_Lab5;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelLinearSearch extends RecursiveTask<Integer> {
	private ArrayList<Integer> arr;
	private int target;
	private int start;
	private int end;
	public static final long serialVersionUID = 1;
	public ParallelLinearSearch(ArrayList<Integer> arr, int target, int start, int end) {
		ForkJoinPool forkJoinPool = new ForkJoinPool(4);
//		ParallelThread myThread = new 
		this.arr = arr;
		this.target = target;
		this.start = start;
		this.end = end;
	}
	public Integer compute() {	// boolean because 
		long timeBefore = System.currentTimeMillis();
		int index = -1;
		for(int i=0; i < arr.size(); i++) {
			if (arr.get(i) == target) {
				index = i;
				break;
			}
		}
		long timeAfter = System.currentTimeMillis();
		System.out.println("Time elapsed for ParallelLinearSearch = " + (timeAfter-timeBefore));
		System.out.println("Searched arr " + arr + " for " + target + ", index = " + index);
		return index;
	}
}
