package asgopina_CS201L_Lab5;


import java.security.Provider.Service;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class MultiThread{
	private int index = -1;
	private long timeElapsed = 0;
	private boolean flag = false;
	private String threadNameString = "";
	private	ExecutorService service;
	
	public MultiThread(ArrayList<Integer> arr, int	 n, int k) {
		Service = ExecutorService.newFixedThreadPool(4);
		Service.execute(new LinearThread(0, (n/4)-1, "T1", k, arr, System.nanoTime(), this);
		Service.execute(new LinearThread((n/4)-1, (n/2)-1, "T2", k, arr, System.nanoTime(), this);
		Service.execute(new LinearThread((n/2)-1, (3*n/4)-1, "T3", k, arr, System.nanoTime(), this);
		Service.execute(new LinearThread((3*n/4)-1, n-1, "T4", k, arr, System.nanoTime(), this);
		Service.shutdown();
		while(!flag && !Service.isTerminated()) {
			Thread.yield();
		}
		Service.shutdownNow();
	}
	public void foundElement(int i, long t, String threadName) {
		if (!flag) {
			if(i != -1) {
				flag = true;
				this.index = i;
				this.timeElapsed = t/1000000;
				this.threadNameString = threadName;
			}
			else if(index == -1) {
				this.timeElapsed = t/1000000;
			}
		}
//		long timeBefore = System.currentTimeMillis();
//		int index = -1;
//		for(int i=start; i<end; i++) {
//			if(arr.get(i) == target) {
//				index = i;
//				break;
//			}
//		}
//		long timeAfter = System.currentTimeMillis();
//		System.out.println("Time elapsed for MultiThreadedLinearSearch = " + (timeAfter-timeBefore) + " ms");
//		System.out.println("\tSearched arr for " + target + ", found at index = " + index);

	}
}

