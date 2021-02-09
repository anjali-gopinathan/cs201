public class MultithreadSleep {
	public static void main(String [] args) {
		System.out.println("First Line");
		Thread ta = new Thread(new TestThread('a'));
		Thread tb = new Thread(new TestThread('b'));
		Thread tc = new Thread(new TestThread('c'));
		ta.start();
		tb.start();
		tc.start();
		ta.interrupt(); // 6	
		System.out.println("Last Line");
	}
}

 class TestThread implements Runnable {
	 private char ch;
	 public TestThread(char ch) {
		 this.ch = ch;		 
	 }
	 public void run() {
		 try { // 2
			 for (int i=0; i < 20; i++) {
				 System.out.print(this.ch + "" + i);
				 Thread.sleep(500); // 1
			 }
			 System.out.println();
		 } catch (InterruptedException ie) { // 3
			 System.out.println("Interrrupted - " + ie.getMessage()); // 4
		  } // 5
	 }
}