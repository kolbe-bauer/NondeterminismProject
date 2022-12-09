package test;

import java.util.ConcurrentModificationException;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.ConnectionPoolDataSource;

import org.apache.commons.dbcp.datasources.PerUserPoolDataSource;

public class PerUserPoolDataSourceTest {

	public static void main(String[] args) {
		new PerUserPoolDataSourceTest().run();
	}
	
	private void run() {
		ReentrantLock lock = new ReentrantLock();
		final org.apache.commons.dbcp.datasources.PerUserPoolDataSource var0 = new org.apache.commons.dbcp.datasources.PerUserPoolDataSource();
		final boolean var1 = var0.getTestWhileIdle();
		final int var2 = var0.getNumIdle();
		final int var3 = var0.getDefaultMaxIdle();
		var0.setDataSourceName((java.lang.String) null);
		final boolean var4 = var0.isRollbackAfterValidation();

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					var0.close();
					final boolean var5 = var0.isTestOnBorrow();
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
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					var0.setConnectionPoolDataSource((javax.sql.ConnectionPoolDataSource) null);
					final int var5 = var0.getNumIdle((java.lang.String) null, (java.lang.String) null);
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
