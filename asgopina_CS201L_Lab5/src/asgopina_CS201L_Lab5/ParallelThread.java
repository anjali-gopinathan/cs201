package asgopina_CS201L_Lab5;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelThread extends RecursiveTask<Integer> {
	private static long notFound = -1;
	private ParallelSearch pSearch;
	private ArrayList<Integer> arr;
	private int target;
	private int start;
	private int end;
	private Long startTime;
	private long size;
	
	public static final long serialVersionUID = 1;
	
	public ParallelThread(ArrayList<Integer> arr, int start, int end, int k, ParallelSearch ps, long sz) {
//		ForkJoinPool forkJoinPool = new ForkJoinPool(4);
//		ParallelThread myThread = new 
		this.arr = arr;
		this.target = target;
		this.start = start;
		this.end = end;
		this.pSearch = ps;
		this.size = sz;
		this.startTime = startTime;
	}
	public Long compute() {
		if(start == -1) {	//break into separate tasks
			List<ParallelThread> subtasks = new ArrayList<ParallelThread>();
			subtasks.add(new ParallelThread(arr, 0, (size/4)-1, target, pSearch, System.nanoTime()))
		}
		
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
