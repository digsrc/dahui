package com.dh.cltf.fw.tcl;

import static org.junit.Assert.assertFalse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tcl.lang.Notifier;


/**
 * This test case test how to drive the Spirent TestCenter TCL script.
 * 
 * 
 * @author Yuan Dahui.
 *
 */
public class StcScriptTest {
	private final static Log LOG = LogFactory.getLog(TclExecutorTests.class);
	
	@Autowired
	private TclExecutorFactory stcTclExecutor;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}
	
	@Test
	public void testExecuteStcScript() {
	}
}
