package thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageTest {
	public static void main(String [] args) {
		for(int i=0; i<2; i++) {
			System.out.println("Starting program iter " + i);
			MessageQueue messageQueue = new MessageQueue();
			
			Messenger messenger = new Messenger(messageQueue);
			Subscriber subscriber = new Subscriber(messageQueue);
			
			ExecutorService executor = Executors.newFixedThreadPool(3);
			executor.execute(messenger);
			executor.execute(subscriber);
			
			executor.shutdown();
			
			while(!executor.isTerminated()) {
				Thread.yield();
			}
			System.out.println("Done\n");
		}
	}
}
