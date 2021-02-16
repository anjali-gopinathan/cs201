import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.print.event.PrintJobAttributeEvent;

public class SleepingBarber extends Thread {

	private static int maxSeats;
	private static int totalCustomers;
	private static ArrayList<Customer> customersWaiting;
	private Lock barberLock;
	private static Condition sleepingCondition;
	private boolean moreCustomers;
	private String barberName;
	private boolean barberIsSleeping;
	
	public SleepingBarber(String barberName_) {
		maxSeats = 3;
		totalCustomers = 10;
		moreCustomers = true;
		customersWaiting = new ArrayList<Customer>();		// need to make this thread safe
		barberLock = new ReentrantLock();
		sleepingCondition = barberLock.newCondition();
		barberName = barberName_;
		barberIsSleeping = true;
		this.start();
	}
	public synchronized static boolean addCustomerToWaiting(Customer customer) {		// static lets the method be shared across several instances as a class method
		if (customersWaiting.size() == maxSeats) {
			return false;
		}
		Util.printMessage("Customer " + customer.getCustomerName() + " is waiting for barber " + barberName);
		customersWaiting.add(customer);
		String customersString = "";
		for (int i=0; i < customersWaiting.size(); i++) {
			customersString += customersWaiting.get(i).getCustomerName();
			if (i < customersWaiting.size() - 1) {
				customersString += ",";
			}
		}
		Util.printMessage("Customers currently waiting for " + barberName + ": " + customersString);
		return true;
	}
	public void wakeUpBarber() {
		try {
			barberLock.lock();
			sleepingCondition.signal();
		} finally {
			barberLock.unlock();
		}
	}
	public void run() {
		while(moreCustomers) {
			while(!customersWaiting.isEmpty()) {
				Customer customer = null;
				synchronized(this) {
					customer = customersWaiting.remove(0);
				}
				customer.startingHaircut();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					System.out.println("ie cutting customer's hair" + ie.getMessage());
				}
				customer.finishingHaircut();
				Util.printMessage("Checking for more customers...");		
			}
			try {
				barberLock.lock();
				Util.printMessage("No customers, so time for " + barberName + " to sleep...");
				sleepingCondition.await();
				Util.printMessage("Someone woke me up! - " + barberName);
			} catch (InterruptedException ie) {
				System.out.println("ie while sleeping: " + ie.getMessage());
			} finally {
				barberLock.unlock();
			}
		}
		Util.printMessage("All done for today!  Time for " + barberName + " to go home!");
		
	}
	public static void main(String [] args) {
		SleepingBarber sb1 = new SleepingBarber("Mumford SB1");
		SleepingBarber sb2 = new SleepingBarber("Sons SB2");
		ExecutorService executors = Executors.newCachedThreadPool();
		for (int i=0; i < sb.totalCustomers; i++) {
			Customer customer = new Customer(i, sb1, sb2);
			executors.execute(customer);
			try {
				Random rand = new Random();
				int timeBetweenCustomers = rand.nextInt(2000);
				Thread.sleep(timeBetweenCustomers);
			} catch (InterruptedException ie) {
				System.out.println("ie in customers entering: " + ie.getMessage());
			}
		}
		executors.shutdown();
		while(!executors.isTerminated()) {
			Thread.yield();
		}
		Util.printMessage("No more customers coming today...");
		sb1.moreCustomers = false;
		sb1.wakeUpBarber();

		sb2.moreCustomers = false;
		sb2.wakeUpBarber();

	}
}
