package asgopina_CSCI201L_Lab5;

import java.util.ArrayList;

public class MultiThreadedLinearSearch implements Runnable {
	private ArrayList<Integer> arr;
	private int target;
	private int start;
	private int end;
	public MultiThreadedLinearSearch(ArrayList<Integer> arr, int target, int start, int end) {
		this.arr = arr;
		this.target = target;
		this.start = start;
		this.end = end;
	}
	public void run() {
		long timeBefore = System.currentTimeMillis();
		int index = -1;
		for(int i=start; i<end; i++) {
			if(arr.get(i) == target) {
				index = i;
				break;
			}
		}
		long timeAfter = System.currentTimeMillis();
		System.out.println("Time elapsed for MultiThreadedLinearSearch = " + (timeAfter-timeBefore) + " ms");
		System.out.println("\tSearched arr for " + target + ", found at index = " + index);

	}
}

