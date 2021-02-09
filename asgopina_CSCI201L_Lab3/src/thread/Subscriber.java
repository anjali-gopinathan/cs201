package thread;

public class Subscriber extends Thread {
	private MessageQueue messageQueue;
	public Subscriber(MessageQueue mq) {
		messageQueue = mq;
	}
	public void run() {
		int numMessagesRead = 0;
		while(numMessagesRead < 20) {
			String msg = messageQueue.getMessage();
			if(msg.length() != 0) {
				System.out.println(Util.getCurrentTime() + " Subscriber - read \"" + msg + "\"");
				numMessagesRead++;
			}
			else {
				System.out.println(Util.getCurrentTime() + " Subscriber - tried to read but no message...");
			}
			try {
				Thread.sleep((long)(Math.random() * 1000));		// sleep for random amount of time btwn 0 and 1 seconds
			} catch (InterruptedException e) {
				System.out.println("Subscriber - sleep interrupted");
			}	
		}
	}
}
