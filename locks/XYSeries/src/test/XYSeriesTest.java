package test;

import java.util.ConcurrentModificationException;
import java.util.concurrent.locks.ReentrantLock;

import org.jfree.data.TimeSeriesTableModel;
import org.jfree.data.XYSeries;

public class XYSeriesTest {

	public static void main(String[] args) {
		new XYSeriesTest().run();
	}
	
	private void run() {
		ReentrantLock lock = new ReentrantLock();
		final org.jfree.data.XYSeries var0 = new org.jfree.data.XYSeries((java.lang.String) "beDff", (boolean) true);
		final java.lang.String var1 = var0.getDescription();
		final org.jfree.data.XYSeries var2 = var0.createCopy((int) 100, (int) 2);
		var0.addPropertyChangeListener((java.beans.PropertyChangeListener) null);
		final int var3 = var0.getItemCount();
		final org.jfree.data.XYSeries var4 = var0.createCopy((int) var3, (int) var3);
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					var0.add((double) -0.1d, (double) -100.0d);
					var0.removeChangeListener((org.jfree.data.SeriesChangeListener) null);
					lock.unlock();
				} catch (Throwable t) {
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					var0.add((double) 2.0d, (double) 0.0d);
					final java.lang.Object var5 = var0.clone();
					lock.unlock();
				} catch (Throwable t) {
					if (t instanceof ConcurrentModificationException) {
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

