package org.datastruct;

import org.datastruct.concurrent.ObjectPool;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

enum TestE {
	Da, Ne, Yes, No;
}

public class ObjectPoolTest {

	static int time = 1000;
	private ObjectPool pool;

	@Before
	public void setUp() throws IOException {
		pool = new ObjectPool();
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream("ObjectPool.properties"));
		pool.setProperties(properties);
	}

	@Test
	@Ignore
	public void testGetObject() throws Exception {
		long sTime, eTime;
		sTime = System.nanoTime();
		assertEquals(new TestObject1("a", 97, false), pool.getObject(TestObject1.class, "a", 97, true));
		eTime = System.nanoTime();
		assertTrue(time + 1000000 <= eTime - sTime);
		sTime = System.nanoTime();
		assertEquals(new TestObject1("a", 97, false), pool.getObject(TestObject1.class, "a", 97, true));
		sTime = System.nanoTime();
		assertTrue(time + 1000000 > eTime - sTime);
		assertEquals(new TestObject1("ba", 89, false), pool.getObject(TestObject1.class, "ba", 89, true));
		eTime = System.nanoTime();
		assertTrue(time + 1000000 <= eTime - sTime);
		sTime = System.nanoTime();
		assertEquals(new TestObject1("ac", 97, false), pool.getObject(TestObject1.class, "ac", 97, true));
		eTime = System.nanoTime();
		assertTrue(time + 1000000 <= eTime - sTime);
		sTime = System.nanoTime();
		assertEquals(new TestObject1("a", 97, false), pool.getObject(TestObject1.class, "a", 97, true));
		eTime = System.nanoTime();
		assertTrue(time + 1000000 > eTime - sTime);
		sTime = System.nanoTime();
		assertEquals(new TestObject2(TestE.Ne, false), pool.getObject(TestObject2.class, TestE.Ne, true));
		eTime = System.nanoTime();
		assertTrue(time + 1000000 <= eTime - sTime);
		sTime = System.nanoTime();
		assertEquals(new TestObject2(TestE.Yes, false), pool.getObject(TestObject2.class, TestE.Yes, true));
		eTime = System.nanoTime();
		assertTrue(time + 1000000 <= eTime - sTime);
		sTime = System.nanoTime();
		assertEquals(new TestObject2(TestE.Yes, false), pool.getObject(TestObject2.class, TestE.Yes, true));
		eTime = System.nanoTime();
		assertTrue(time + 1000000 > eTime - sTime);
		sTime = System.nanoTime();
		assertEquals(new TestObject2(TestE.Da, false), pool.getObject(TestObject2.class, TestE.Da, true));
		eTime = System.nanoTime();
		assertTrue(time + 1000000 <= eTime - sTime);
		sTime = System.nanoTime();
		assertEquals(new TestObject2(TestE.Ne, false), pool.getObject(TestObject2.class, TestE.Ne, true));
		eTime = System.nanoTime();
		assertTrue(time + 1000000 > eTime - sTime);
		sTime = System.nanoTime();
		assertEquals(new TestObject2(TestE.Yes, false), pool.getObject(TestObject2.class, TestE.Yes, true));
		eTime = System.nanoTime();
		assertTrue(time + 1000000 > eTime - sTime);
	}
}

class TestObject1 {

	private int hash;

	public TestObject1(String s, Integer i, Boolean test) {
		if (test) {
			try {
				Thread.sleep(ObjectPoolTest.time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		hash = s.hashCode() + i.hashCode();
	}

	public int getHash() {
		return hash;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TestObject1)) return false;
		TestObject1 that = (TestObject1) o;
		if (getHash() != that.getHash()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getHash();
	}
}

class TestObject2 {

	private TestE e;

	public TestObject2(TestE e, Boolean test) {
		if (test) {
			try {
				Thread.sleep(ObjectPoolTest.time);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		this.e = e;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TestObject2)) return false;
		TestObject2 that = (TestObject2) o;
		if (e != that.e) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return e != null ? e.hashCode() : 0;
	}
}

