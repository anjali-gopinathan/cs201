package asgopina_CS201L_Lab5;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

public class ParallelSearch {
	private ForkJoinPool forkJoinPool;
	private int index = -1;
	private long timeElapsed = 0;
	private boolean flag = false;
	
	public ParallelSearch(ArrayList<Integer>arrayList, int n, int k ) {
		// TODO Auto-generated constructor stub
		ForkJoinPool f = new ForkJoinPool(4);
		ParallelThread myPThread = new ParallelThread(arr, -1, 0, k, this, System.nanoTime(), arrayList.size());
		long index = f.invoke(myPThread);
		System.out.println("Parallel (" + this.timeElapsed + " ms): " + index);
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public boolean getFlag() {
		return this.flag;
	}
	public void setTimeElapsed(long d) {
		this.timeElapsed = d/1000000;
	}
}
