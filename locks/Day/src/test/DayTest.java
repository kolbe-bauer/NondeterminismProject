package test;

import org.jfree.data.time.Day;

import java.util.concurrent.locks.ReentrantLock;

public class DayTest {

	public static void main(String[] args) {
		new DayTest().run();
	}
	
	private void run() {
		ReentrantLock lock = new ReentrantLock();
		final org.jfree.data.time.Day var0 = new org.jfree.data.time.Day();
		final org.jfree.data.time.RegularTimePeriod var1 = var0.next();
		final java.util.TimeZone var2 = org.jfree.data.time.RegularTimePeriod.DEFAULT_TIME_ZONE;
		final long var3 = var0.getFirstMillisecond((java.util.TimeZone) var2);
		final org.jfree.data.time.RegularTimePeriod var4 = org.jfree.data.time.RegularTimePeriod.createInstance((java.lang.Class) null, (java.util.Date) null, (java.util.TimeZone) null);
		final java.lang.String var5 = var0.toString();
		final long var6 = var0.getSerialIndex();
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					final long var7 = var0.getLastMillisecond();
					final org.jfree.data.time.Day var8 = org.jfree.data.time.Day.parseDay((java.lang.String) var5);
					lock.unlock();
				} catch (Throwable t) {
					if (t instanceof NumberFormatException) {
						System.out.println("\n--------------------\nBug found:\n");
						t.printStackTrace(System.out);
						System.out.println("---------------------\n");
						System.exit(0);
					}
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					final long var7 = var0.getLastMillisecond();
					final org.jfree.data.time.Day var8 = org.jfree.data.time.Day.parseDay((java.lang.String) var5);
					lock.unlock();
				} catch (Throwable t) {
					if (t instanceof NumberFormatException) {
						System.out.println("\n--------------------\nBug found:\n");
						t.printStackTrace(System.out);
						System.out.println("---------------------\n");
						System.exit(0);
					}
				}
			}
		});

		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

}
