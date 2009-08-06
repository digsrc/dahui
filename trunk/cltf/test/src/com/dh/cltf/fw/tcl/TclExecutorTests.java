package com.dh.cltf.fw.tcl;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tcl.lang.Notifier;

/**
 * Test case for TclExecutorFactory.
 * 
 * @author Yuan Dahui.
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testBeans.xml")
public class TclExecutorTests {
	private final static Log LOG = LogFactory.getLog(TclExecutorTests.class);
	
	@Autowired
	private TclExecutor executorA; // = TclExecutorFactory.getTclExecutor("TclInterpreter-A");
	
	@Autowired
	private TclExecutor executorB; // = TclExecutorFactory.getTclExecutor("TclInterpreter-B");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}
	
	
	/**
	 * The different TclExecutor has its own interpreter instance.
	 */
	@Test
	public void testTclNotifiers() {
		// Each TclExecutor's interpreter thread has its own notifier object.
		Notifier notifierA = executorA.getTclInterpreter().getNotifier();
		Notifier notifierB = executorB.getTclInterpreter().getNotifier();
		
		boolean isSameNotifier = (notifierA == notifierB);
		
		assertFalse(isSameNotifier);
	}
	
	
	/**
	 * Set the variable in different TCL interpreter.
	 */
	@Test
	public void testSetVariable() {
		String result = null;
		String variableValue = "bbb";
		
		result = executorA.execute("set varB \"" + variableValue + "\"");
		assertTrue(variableValue.equals(result));
		
		result = executorA.execute("puts $varB");
		
		result = executorB.execute("puts $varB");
		boolean foundErr = result.indexOf("no such variable") > 0;
		assertTrue(foundErr);
	}
	
	/**
	 * Test stop the TCL interpreter.
	 * 
	 * @throws InterruptedException exception
	 */
	@Test
	public void testStopTclExecutor() throws InterruptedException {
		final int sleepTime = 200;
		
		assertFalse(executorA.isTclInterpClosed());
		assertFalse(executorB.isTclInterpClosed());
		
		TclExecutorManager.getInstance().dispose(executorA.getName());
		TclExecutorManager.getInstance().dispose(executorB.getName());

		LOG.debug("testStopTclExecutor()   " + executorA.isTclInterpClosed());
		
		// TCL interpreter runs in another thread, so we need some time to wait it finishing stop.
		Thread.sleep(sleepTime);
		
		assertTrue(executorA.isTclInterpClosed());
		assertTrue(executorB.isTclInterpClosed());
	}

}
