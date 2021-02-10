package thread;

public class Messenger extends Thread {
	// Goal: to add specified messages to the MessageQueue
	private MessageQueue messageQueue;
	public Messenger(MessageQueue mq) {
		messageQueue = mq;
	}
	public void run() {
		for(int i=0; i<20; i++) {
			String msg = "message #" + i;
			System.out.println(Util.getCurrentTime() + " Messenger - insert \"" + msg + "\"");
			messageQueue.addMessage(msg);
			
			try {
				Thread.sleep((long)(Math.random() * 1000));		// sleep for random amount of time btwn 0 and 1 seconds
			} catch (InterruptedException e) {
				System.out.println("Messenger message # " + i + " sleep interrupted");
			}	
		}
	}
}
