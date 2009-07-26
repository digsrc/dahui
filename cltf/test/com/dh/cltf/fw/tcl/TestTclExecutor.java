package com.dh.cltf.fw.tcl;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test case for TclExecutor.
 * 
 * @author Yuan Dahui.
 *
 */
public class TestTclExecutor {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}
	
	/**
	 * Test stop the TCL interpreter.
	 * 
	 * @throws InterruptedException exception
	 */
	@Test
	public void testStopTclExecutor() throws InterruptedException {
		TclExecutor executorA = TclExecutor.getTclExecutor("TclInterpreter-A");
		TclExecutor executorB = TclExecutor.getTclExecutor("TclInterpreter-B");
		
		boolean isSameNotifier = 
			executorA.getTclInterpreter().getNotifier() == executorB.getTclInterpreter().getNotifier();
		// Each TclExecutor's interpreter thread has its own notifier object.
		assertTrue(! isSameNotifier);
		
		assertTrue(executorA.isTclInterpClosed());
		assertTrue(executorB.isTclInterpClosed());
		
		executorA.dispose();
		executorB.dispose();
		
		// TCL interpreter runs in another thread, so we need some time to wait it finishing stop.
		final int sleepTime = 200;
		Thread.sleep(sleepTime);
		
		assertTrue(!executorA.isTclInterpClosed());
		assertTrue(!executorB.isTclInterpClosed());
	}
}
