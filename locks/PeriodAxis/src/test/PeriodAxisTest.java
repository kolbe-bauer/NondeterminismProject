package test;

import org.jfree.chart.axis.PeriodAxis;

import java.util.concurrent.locks.ReentrantLock;

public class PeriodAxisTest {

	public static void main(String[] args) {
		new PeriodAxisTest().run();
	}
	
	private void run() {
		ReentrantLock lock = new ReentrantLock();
		final org.jfree.chart.axis.PeriodAxis var0 = new org.jfree.chart.axis.PeriodAxis((java.lang.String) null);
		final org.jfree.data.time.RegularTimePeriod var1 = var0.getFirst();
		final org.jfree.chart.axis.PeriodAxis var2 = new org.jfree.chart.axis.PeriodAxis((java.lang.String) null, (org.jfree.data.time.RegularTimePeriod) var1, (org.jfree.data.time.RegularTimePeriod) var1);
		var2.setUpperBound((double) 0.0d);
		final org.jfree.chart.axis.PeriodAxis var3 = new org.jfree.chart.axis.PeriodAxis((java.lang.String) null);
		final java.awt.Font var4 = var3.getTickLabelFont();
		var2.setLabelFont((java.awt.Font) var4);
		final java.awt.Paint var5 = var2.getAxisLinePaint();
		final java.awt.Font var6 = var2.getTickLabelFont();
		final org.jfree.data.Range var7 = var3.getRange();
		var2.setRange((org.jfree.data.Range) var7, (boolean) true, (boolean) true);
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					final org.jfree.chart.axis.TickUnitSource var8 = var0.getStandardTickUnits();
					var2.setStandardTickUnits((org.jfree.chart.axis.TickUnitSource) var8);
					var2.setRangeWithMargins((double) -3.0d, (double) 0.0d);
					lock.unlock();
				} catch (Throwable t) {
					if (t instanceof IllegalArgumentException) {
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
					final java.lang.Class var8 = var2.getMinorTickTimePeriodClass();
					final org.jfree.data.Range var9 = var2.getRange();
					lock.unlock();
				} catch (Throwable t) {
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
