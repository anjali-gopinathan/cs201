   import java.util.concurrent.ForkJoinPool;
   import java.util.concurrent.RecursiveAction;
   public class ParallelMergeSort {
     public static void main(String[] args) {
       int SIZE = 2_000_000;
       int[] list = new int[SIZE];
        
       for (int i = 0; i < SIZE; i++) {
         list[i] = (int)(Math.random() * Integer.MAX_VALUE);
      }   
      int numProcessors = Runtime.getRuntime().availableProcessors();
      System.out.println("num processors: " + numProcessors);
   
      // timing[i] : time to sort on i processors
      long[] timing = new long[numProcessors*2+1];
   
      for (int i=1; i <= numProcessors * 2; i++) {
        timing[i] = parallelMergeSort((int[])list.clone(), i);
        System.out.println(i + " processors=" + timing[i] + " ms");
      }    
    }
    
    public static long parallelMergeSort(int[] list, int proc) {
      long before = System.currentTimeMillis();
      ForkJoinPool pool = new ForkJoinPool(proc);
      pool.invoke(new SortTask(list));
      pool.shutdown();
      while (!pool.isTerminated()) { 
        Thread.yield();
      }
      long after = System.currentTimeMillis();
      long time = after - before;
      return time;
    }  
    private static class SortTask extends RecursiveAction {
      public static final long serialVersionUID = 1;
      private int[] list;
      SortTask(int[] list) { 
        this.list = list; 
      }
        
      protected void compute() {
        if (list.length < 2) return; // base case
        // split into halves  
        int[] firstHalf = new int[list.length / 2];
        System.arraycopy(list, 0, firstHalf, 0, list.length / 2);
        int secondLength = list.length - list.length / 2;
        int[] secondHalf = new int[secondLength];
        System.arraycopy(list, list.length / 2, secondHalf, 0, secondLength);
                
        // recursively sort the two halves
        SortTask st1 = new SortTask(firstHalf);
        SortTask st2 = new SortTask(secondHalf);
        st1.fork();
        st2.fork();
        st1.join();
        st2.join();
        // merge halves together
        merge(firstHalf, secondHalf, list);
      }
    }
   
    public static void merge(int[] list1, int[] list2, int[] merged) {
      int i1 = 0, i2 = 0, i3 = 0; // index in list1, list2, out
      while (i1 < list1.length && i2 < list2.length) {
        merged[i3++] = (list1[i1] < list2[i2]) ? list1[i1++] : list2[i2++];
      }      
      // any trailing ends        
      while (i1 < list1.length) { 
        merged[i3++] = list1[i1++];
      }
      while (i2 < list2.length) {
        merged[i3++] = list2[i2++];
      }
    }
  }
    
