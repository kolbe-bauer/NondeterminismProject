package test;

import java.util.ConcurrentModificationException;
import java.util.concurrent.locks.ReentrantLock;

import org.jfree.chart.plot.XYPlot;

public class XYPlotTest {

	public static void main(String[] args) {
		new XYPlotTest().run();
	}
	
	private void run() {
		ReentrantLock lock = new ReentrantLock();
		final org.jfree.chart.plot.XYPlot var0 = new org.jfree.chart.plot.XYPlot();
		final org.jfree.chart.renderer.xy.XYItemRenderer var1 = var0.getRenderer();
		final double var2 = var0.getDomainCrosshairValue();
		final java.awt.Rectangle var3 = new java.awt.Rectangle((int) 100, (int) 0, (int) 3, (int) 0);
		final java.awt.Rectangle var4 = var3.getBounds();
		final java.awt.Rectangle var5 = var4.union((java.awt.Rectangle) var3);
		final java.awt.Point var6 = var5.getLocation();
		var0.zoomDomainAxes((double) 0.0d, (double) var2, (org.jfree.chart.plot.PlotRenderingInfo) null, (java.awt.geom.Point2D) var6);
		final int var7 = var0.getDomainAxisCount();
		final org.jfree.chart.renderer.xy.XYItemRenderer var8 = var0.getRendererForDataset((org.jfree.data.xy.XYDataset) null);
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					final org.jfree.chart.axis.AxisLocation var9 = var0.getDomainAxisLocation((int) 3);
					var0.mapDatasetToRangeAxis((int) var7, (int) var7);
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
					final java.lang.Object var9 = var0.clone();
					var0.setDomainCrosshairValue((double) var2);
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
 