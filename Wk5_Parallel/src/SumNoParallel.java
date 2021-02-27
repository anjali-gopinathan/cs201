  public class SumNoParallel {
    public static void main(String [] args) {
      long minNumber = 0;
      long maxNumber = 1_000_000_000; 
      long before = System.currentTimeMillis();
      Sum sum = new Sum(minNumber, maxNumber);
      long num = sum.compute();
      long after = System.currentTimeMillis();
      System.out.println("time without parallelism = " + (after - before));
      System.out.println("SUM(" + minNumber + ".." + maxNumber + ") = " + num);
   }
  }
  
  class Sum {
     private long minNum;
     private long maxNum;
     Sum(long minNum, long maxNum) { 
       this.minNum = minNum;
       this.maxNum = maxNum; 
     }
     protected Long compute() {
       long sum = 0;
       for (long i=minNum; i <= maxNum; i++) {
         sum += i;
       }
       return sum;
     }
   }

