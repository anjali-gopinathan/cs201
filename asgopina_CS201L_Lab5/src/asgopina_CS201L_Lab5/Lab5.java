package asgopina_CS201L_Lab5;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;


public class Lab5 {
	public static void main(String[] args) {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (int i=0; i<100000; i++) {
			arr.add(i);
		}
		System.out.println("arr size = " + arr.size());
		
		
		Collections.shuffle(arr);
		Random rand = new Random();	// creates new random number generator, seed 0
		int randInt = rand.nextInt(arr.size());

		int badInt = -5;
		
		// single threaded
		SingleThreadedLinearSearch single = new SingleThreadedLinearSearch(arr);
		single.linearSearch(randInt);

		// multithreaded
		int numThreads = 4;
//		new MultiThreadedLinearSearch(arr,  badInt, numThreads, randInt)

		for (int i=0; i<numThreads; i++) {
			MultiThread multi = new MultiThread(arr, randInt, i * arr.size() / numThreads, (i+1) * arr.size()/numThreads);
			Thread t = new Thread(multi);
			t.start();
		}
		int numProcessors = Runtime.getRuntime().availableProcessors();
		ForkJoinPool pool = new ForkJoinPool(numProcessors);
//		pool.invoke(ParallelLinearSearch)
		System.out.println("num processors: " + numProcessors);
		for(int i=0; i<numProcessors*2; i++) {
			ParallelLinearSearch parallel = new ParallelLinearSearch(arr, randInt, i * arr.size() / numThreads, (i+1) * arr.size()/numThreads);
//			parallel.compute();
			pool.invoke(parallel);
		}
		pool.shutdown();
		
		ParallelLinearSearch p[] = new ParallelLinearSearch[numThreads];
		for(int i=0; i<numThreads; i++) {
			ParallelLinearSearch parallel = new ParallelLinearSearch(arr, randInt, i * arr.size() / numThreads, (i+1) * arr.size()/numThreads);
//			p[i] = parallel;
			parallel.compute();
			pool.execute(parallel);
		}

		long num = 0;
		for (int i=0; i < numThreads; i++) {
			num += p[i].join();
		}

		
//		Thread t1 = new Thread(single1);
//		t1.start();
//		
//		
//		SingleThreadedLinearSearch single2 = new SingleThreadedLinearSearch(arr, badInt);
//		Thread t2 = new Thread(single2);
//		t2.start();		
		

		
//		System.out.println("----- searching for " + badInt + "-----");
//		single.linearSearch(badInt);
//		// multithreaded
//		for (int i=0; i<numThreads; i++) {
//			MultiThreadedLinearSearch multi = new MultiThreadedLinearSearch(arr, badInt, i * arr.size() / numThreads, (i+1) * arr.size()/numThreads);
//			Thread t = new Thread(multi);
//			t.start();
//		}
//		for(int i=0; i<numThreads; i++) {
//			ParallelLinearSearch parallel = new ParallelLinearSearch(arr, badInt, i * arr.size() / numThreads, (i+1) * arr.size()/numThreads);
////			p[i] = parallel;
//			parallel.compute();
////			pool.execute(parallel);
//		}
//		pool.shutdown();
		while (!pool.isTerminated()) { 
			Thread.yield();
		}
	}
}
