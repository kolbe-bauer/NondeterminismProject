package test;

import java.io.ByteArrayOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.SjsxpDriver;

public class XStreamTest {

	public static void main(String[] args) throws Exception {
		new XStreamTest().run();
	}
	
	private void run() throws Exception {
		ReentrantLock lock = new ReentrantLock();
		final com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider var0 = new com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider();
		final com.thoughtworks.xstream.XStream var1 = new com.thoughtworks.xstream.XStream((com.thoughtworks.xstream.converters.reflection.ReflectionProvider) var0);
		final java.lang.ClassLoader var2 = java.lang.ClassLoader.getSystemClassLoader();
		var1.setClassLoader((java.lang.ClassLoader) var2);
		final com.thoughtworks.xstream.mapper.Mapper var3 = var1.getMapper();
		var1.autodetectAnnotations((boolean) false);
		final java.io.ObjectOutputStream var4 = var1.createObjectOutputStream((java.io.Writer) null, (java.lang.String) "");
		var1.addDefaultImplementation((java.lang.Class) null, (java.lang.Class) null);

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					final java.lang.String var5 = var1.toXML((java.lang.Object) var0);
					var1.useAttributeFor((java.lang.Class) null);
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
					var1.registerConverter((com.thoughtworks.xstream.converters.SingleValueConverter) null);
					var1.setClassLoader((java.lang.ClassLoader) var2);
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
