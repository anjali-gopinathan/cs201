   import java.util.LinkedList;
   import java.util.concurrent.ExecutorService;
   import java.util.concurrent.Executors;

// NOTE: this code can cause a deadlock with one thread waiting on notEmpty while the
// other thread is waiting on notFull, yet both threads are holding onto the other lock
   public class ProducerConsumerWithMonitors {
     private static Buffer buffer = new Buffer();

    public static void main(String [] args) {
      ExecutorService executor = Executors.newFixedThreadPool(2);	//2 tasks
      //wait till we do the work
      executor.execute(new ProducerTask());
      executor.execute(new ConsumerTask());
      //shutdown.
      executor.shutdown();
    }

    private static class ProducerTask implements Runnable {
      public void run() {
        try {
          int i = 1;
          while (true) {
            System.out.println("Producer trying to write: " + i);
            buffer.write(i++);	//add soda (producer)
            Thread.sleep((int)(Math.random() * 1000));
          }
        } catch (InterruptedException ie) {
          System.out.println("Producer IE: " + ie.getMessage());
        }
      }
    }

    private static class ConsumerTask implements Runnable {
      public void run() {
        try {
          while (true) {
            System.out.println("\t\tConsumer reads: " + buffer.read());
            Thread.sleep((int)(Math.random() * 1000));
          }
        } catch (InterruptedException ie) {
          System.out.println("Consumer IE: " + ie.getMessage());
        }
      }
    }
     private static class Buffer {
       private static final int CAPACITY = 1;		// only one slot for the soda
       private LinkedList<Integer> queue = new LinkedList<Integer>();
       private static Object notEmpty = new Object();		
       private static Object notFull = new Object();

       public void write(int value) {
    	   synchronized(notFull) {		//synchronized: notFull is locked. 
    		   synchronized(notEmpty) {
    			   try {
    				   // wait for not full condition. this waits until notFull notify gets executed. if notFull 
    				   while (queue.size() == CAPACITY) {
    					   System.out.println("Wait for notFull condition on " + value);
    					   notFull.wait();
    				   }
    				   queue.offer(value);
    				   notEmpty.notify();		// wakes up 
    			   } catch (InterruptedException ie) {
    				   System.out.println("Buffer.write IE: " + ie.getMessage());
    			   }
    		   }
    	   }
       }

       public int read() {
         int value = 0;
         synchronized(notFull) {
        	 synchronized(notEmpty) {
        		 try {
		           while (queue.isEmpty()) {
		             System.out.println("\t\tWait for notEmpty condition");
		             notEmpty.wait();
		           }
		           value = queue.remove();	//take soda
		           notFull.notify();
		         } catch (InterruptedException ie) {
		           System.out.println("Buffer.read IE: " + ie.getMessage());
		         }
	         }
         }
         return value;
       }
     } // ends class Buffer
   } // ends class ConsumerProducer


