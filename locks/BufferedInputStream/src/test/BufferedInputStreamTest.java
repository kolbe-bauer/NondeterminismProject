package test;

import java11.io.BufferedInputStream;
import java11.io.StringBufferInputStream;

import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("deprecation")
public class BufferedInputStreamTest {

	public static void main(String[] args) throws Exception {
		new BufferedInputStreamTest().run();
	}
	
	private void run() throws Exception {
		ReentrantLock lock = new ReentrantLock();
		final java11.io.StringBufferInputStream var0 = new java11.io.StringBufferInputStream((java.lang.String) "XE.]V");
		final java11.io.BufferedInputStream var1 = new java11.io.BufferedInputStream((java11.io.InputStream) var0);
		final boolean var2 = var1.markSupported();
		var1.mark((int) 1);
		final long var3 = var1.skip((long) 100L);
		var1.mark((int) 3);
		final long var4 = var1.skip((long) 3L);

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					final long var5 = var1.skip((long) var3);
					final int var6 = var1.read();
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
					var1.close();
					var1.close();
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
