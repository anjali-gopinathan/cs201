package asgopina_CS201L_Lab5;


import java.awt.desktop.ScreenSleepEvent;
import java.util.ArrayList;


public class SingleThreadedLinearSearch {
	private ArrayList<Integer> arr;
//	private int target;
	public SingleThreadedLinearSearch(ArrayList<Integer> arr) {
		this.arr = arr;
//		this.target = target;
	}
	public void linearSearch(int target) {
		long timeBefore = System.currentTimeMillis();
		int index = -1;
		for(int i=0; i < arr.size(); i++) {
			if (arr.get(i) == target) {
				index = i;
				break;
			}
			
		}
		long timeAfter = System.currentTimeMillis();
		long elapsed = timeAfter-timeBefore;
		System.out.println("Time elapsed for SingleThreadedLinearSearch = " + elapsed + " ms");
		System.out.println("\tSearched arr for " + target + ", found at index = " + index);
	}
	
}
