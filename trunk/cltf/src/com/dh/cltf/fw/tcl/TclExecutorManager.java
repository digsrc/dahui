package com.dh.cltf.fw.tcl;

import java.util.Collection;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tcl.lang.Interp;
import tcl.lang.TCL;
import tcl.lang.TclEvent;
import tcl.lang.TclException;
import tcl.lang.TclObject;


/**
 * To execute a TCL command or a source file, just create an instance of this 
 * class.  Different instance of this class has its own TCL interpreter.
 * 
 * 
 * @author Yuan Dahui.
 *
 */
public class TclExecutorManager {
	private static final Log LOG = LogFactory.getLog(TclExecutorManager.class);
	
	private static TclExecutorManager TCLEXECUTOR_MANAGER = new TclExecutorManager();
	
	private static Hashtable<String, TclExecutor> tclExecutors = new Hashtable<String, TclExecutor>();
	

	
	/**
	 * Constructor. Create and start the interpreter's process thread.
	 * @param name executor name.
	 */
	private TclExecutorManager() {
	}
	
	/**
	 * TclExecutorManager is a sigelton.
	 * 
	 * @return
	 */
	public static TclExecutorManager getInstance() {
		return TCLEXECUTOR_MANAGER;
	}



	
	
	/**
	 * Close executor and its interpreter thread.
	 */
	public void dispose(String name) {
		synchronized (tclExecutors) {
			TclExecutor executor = tclExecutors.remove(name);
			executor.tclInterpThread.setStop(true);
			executor.tclInterpThread.interrupt();
			executor.execute("puts \" interpreter[" + executor.getName() + "] closed.\"");
		}
	}

	public void disposeAll() {
		synchronized (tclExecutors) {
			Collection<TclExecutor> exes = tclExecutors.values();
			for (TclExecutor executor: exes) {
				tclExecutors.remove(executor.getName());
				executor.tclInterpThread.setStop(true);
				executor.tclInterpThread.interrupt();
				
				executor.execute("puts \" interpreter[" + executor.getName() + "] closed.\"");
			}
		}
	}
	
	public String executeTclFile(String name, String tclFile) {
		TclExecutor executor = getTclExecutor(name);
		return executor.executeTclFile(tclFile);
	}
	
	public String execute(String name, String command) {
		TclExecutor executor = getTclExecutor(name);
		return executor.execute(command);
	}

	/**
	 * Get default TCL executor.
	 * 
	 * @return executor
	 */
	public TclExecutor getTclExecutor() {
		return getTclExecutor("default");
	}
	
	
	/**
	 * Get a named TCL executor. 
	 * If the named TCL executor does not exist, it will be created.
	 * This is a factory method.
	 * 
	 * @param name executor name.
	 * @return executor
	 */
	public TclExecutor getTclExecutor(String name) {
		synchronized (tclExecutors) {
			if (tclExecutors.get(name) == null) {
				tclExecutors.put(name, new TclExecutor(name));
			}
			return tclExecutors.get(name);		
		}
	}
	
	

	/**
	 * Test method.
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {
		TclExecutor e = TclExecutorManager.getInstance().getTclExecutor("MyTcl");
		for (int i = 0; i < 2; i ++) {
			e.execute("puts \"abc1\"");
			e.execute("puts \"abc2\"");
		}
		TclExecutor e2 = TclExecutorManager.getInstance().getTclExecutor("tester");
		for (int i = 0; i < 3; i ++) {
			e2.execute("puts \"123a\"");
			e2.execute("puts \"123b\"");
		}
	}
}
