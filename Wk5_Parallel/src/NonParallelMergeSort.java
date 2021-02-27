public class NonParallelMergeSort {
     public static void main(String[] args) {
       int SIZE = 2_000_000;
       int[] list = new int[SIZE];
        
       for (int i = 0; i < SIZE; i++) {
         list[i] = (int)(Math.random() * Integer.MAX_VALUE);
      }   
   
      long timing = 0;
      long sum = 0;
      // run it 8 times to see if there are variations
      for (int i=0; i < 8; i++) {
	      timing = nonParallelMergeSort((int[])list.clone());
	      System.out.println(timing + " ms");
	      sum += timing;
      }
      System.out.println("average = " + (sum / 8) + " ms");
    }
    
    public static long nonParallelMergeSort(int[] list) {
      long before = System.currentTimeMillis();
      new SortTask(list).compute();
      long after = System.currentTimeMillis();
      long time = after - before;
      return time;
    }  
    private static class SortTask {
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
        new SortTask(firstHalf).compute();
        new SortTask(secondHalf).compute();
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
    
