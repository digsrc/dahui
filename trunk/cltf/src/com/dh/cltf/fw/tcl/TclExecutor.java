package com.dh.cltf.fw.tcl;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tcl.lang.Interp;
import tcl.lang.TCL;
import tcl.lang.TclEvent;
import tcl.lang.TclException;


/**
 * To execute a TCL command or a source file, just create an instance of this 
 * class.  Different instance of this class has its own TCL interpreter.
 * 
 * 
 * @author Yuan Dahui.
 *
 */
public class TclExecutor {
	private static final Log LOG = LogFactory.getLog(TclExecutor.class);
	
	private static Hashtable<String, TclExecutor> tclExecutors = new Hashtable<String, TclExecutor>();
	
	/** TCL interpreter */
	private Interp tclInterpreter;


	/** Thread to run the TCL interperter's event processor */
	private TclInterpreterThread tclInterpThread;
	
	/**
	 * Constructor. Create and start the interpreter's process thread.
	 * @param name executor name.
	 */
	private TclExecutor(String name) {
		startInterpreterThread(name);
	}
	
	
	/**
	 * Start the TCL processor thread.
	 * @param name executor name.
	 */
	private void startInterpreterThread(String name) {
		tclInterpThread = new TclInterpreterThread();
		tclInterpThread.setName("Thread-" + name);
		tclInterpThread.setDaemon(true);
		tclInterpThread.start();
		
		// loop until the tcl interpreter started.
		final int sleepTime = 100;
		while (tclInterpThread.getInterp() == null) {
			try {
				Thread.sleep(sleepTime);
				LOG.debug("waiting tcl process thread starting.");
			} catch (InterruptedException e) {
				LOG.warn(e);
			}
		}
		tclInterpreter = tclInterpThread.getInterp();
	}
	
	
	/**
	 * Close executor and its interpreter thread.
	 */
	public void dispose() {
		tclInterpThread.setStop(true);
		tclInterpThread.interrupt();
	}
	
	
	/**
	 * Is TCL interpreter closed.
	 * 
	 * @return is TCL interpreter closed.
	 */
	public boolean isTclInterpClosed() {
		return tclInterpThread.isRunning();
	}
	
	/**
	 * Execute TCL file.
	 * 
	 * @param tclFile   TCL file name
	 * @return result
	 */
	public String executeTclFile(final String tclFile) {
        final StringBuffer result = new StringBuffer();

        TclEvent event = new TclEvent() {
            public int processEvent(int flags) {
                try {
                	String fileName = tclFile.replace('\\', '/');
                    tclInterpreter.evalFile(fileName);
                    result.append(tclInterpreter.getResult().toString());
                } catch (TclException ex) {
                    LOG.warn(ex);
                }
                return 1;
	    }
        };

        // Add event to TCL Event Queue in other thread
        tclInterpreter.getNotifier().queueEvent(event, TCL.QUEUE_TAIL);

        // Wait for event to be processed by the other thread.
        event.sync();

        return result.toString();
	}
	
	
	/**
	 * Execute the tcl command synchronously.
	 * 
	 * @param command command
	 * @return result
	 */
	public String execute(final String command) {
        final StringBuffer result = new StringBuffer();

        TclEvent event = new TclEvent() {
            public int processEvent(int flags) {
                try {
                	LOG.debug(command);
            		tclInterpreter.eval(command);
                    result.append(tclInterpreter.getResult().toString());
                } catch (TclException ex) {
                    LOG.warn(ex);
                }
                return 1;
	    }
        };

        // Add event to TCL Event Queue in other thread
        tclInterpreter.getNotifier().queueEvent(event, TCL.QUEUE_TAIL);

        // Wait for event to be processed by the other thread.
        event.sync();

        return result.toString();
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
	 * 
	 * @param name executor name.
	 * @return executor
	 */
	public static TclExecutor getTclExecutor(String name) {
		synchronized (tclExecutors) {
			if (tclExecutors.get(name) == null) {
				tclExecutors.put(name, new TclExecutor(name));
			}
			return tclExecutors.get(name);		
		}
	}
	
	
	
	protected Interp getTclInterpreter() {
		return tclInterpreter;
	}
	
	/**
	 * Test method.
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {
		TclExecutor e = new TclExecutor("MyTcl");
		for (int i = 0; i < 2; i ++) {
			e.execute("puts \"abc1\"");
			e.execute("puts \"abc2\"");
		}
		TclExecutor e2 = new TclExecutor("tester");
		for (int i = 0; i < 3; i ++) {
			e2.execute("puts \"123a\"");
			e2.execute("puts \"123b\"");
		}
	}
}
