package com.dh.cltf.fw.tcl;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test case for TclExecutor.
 * 
 * @author Yuan Dahui.
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("testBeans.xml")
public class TclExecutorTests {
	
	@Resource
	private TclExecutorFactory executorA = TclExecutorFactory.getTclExecutor("TclInterpreter-A");
	private TclExecutorFactory executorB = TclExecutorFactory.getTclExecutor("TclInterpreter-B");
	
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
