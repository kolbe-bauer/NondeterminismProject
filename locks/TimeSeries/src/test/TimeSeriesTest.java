package test;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;

import java.util.concurrent.locks.ReentrantLock;

public class TimeSeriesTest {

	public static void main(String[] args) {
		new TimeSeriesTest().run();
	}
	
	private void run() {
		ReentrantLock lock = new ReentrantLock();
		final org.jfree.data.time.TimeSeries var0 = new org.jfree.data.time.TimeSeries((java.lang.String) null, (java.lang.Class) null);
		var0.setMaximumItemCount((int) 3);
		var0.removeChangeListener((org.jfree.data.SeriesChangeListener) null);
		final java.lang.Object var1 = var0.clone();
		final org.jfree.data.time.Day var2 = new org.jfree.data.time.Day();
		final org.jfree.data.time.TimeSeriesDataItem var3 = var0.addOrUpdate((org.jfree.data.time.RegularTimePeriod) var2, (java.lang.Number) null);
		var0.setMaximumItemCount((int) -2);

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					var0.delete((org.jfree.data.time.RegularTimePeriod) var2);
					var0.setName((java.lang.String) null);
					lock.unlock();
				} catch (Throwable t) {
					if (t instanceof NullPointerException) {
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
					var0.delete((org.jfree.data.time.RegularTimePeriod) var2);
					var0.setName((java.lang.String) null);
					lock.unlock();
				} catch (Throwable t) {
					if (t instanceof NullPointerException) {
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
