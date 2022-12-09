package test;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java141.util.logging.Logger;

public class LoggerTest {

	public static void main(String[] args) {
		new LoggerTest().run();
	}
	
	private void run() {
		ReentrantLock lock = new ReentrantLock();
		final java141.util.logging.Logger var0 = java141.util.logging.Logger.getLogger((java.lang.String) "");
		var0.finer((java.lang.String) null);
		final java.util.logging.Handler[] var1 = var0.getHandlers();
		var0.throwing((java.lang.String) "", (java.lang.String) "m@,ys{", (java.lang.Throwable) null);
		final java141.util.logging.Logger var2 = java141.util.logging.Logger.getAnonymousLogger((java.lang.String) null);
		final boolean var3 = var0.getUseParentHandlers();
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					var0.setFilter((java.util.logging.Filter) null);
					final java.util.logging.Level var4 = java.util.logging.Level.FINE;
					var0.setLevel((java.util.logging.Level) var4);
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
					var0.warning((java.lang.String) "B%");
					final bug4779253.MyFilter var4 = new bug4779253.MyFilter();
					var0.setFilter((java.util.logging.Filter) var4);
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

	class MyFilter implements Filter {

		public boolean isLoggable(LogRecord record) {
			return true;
		}
		
	}
	
}
