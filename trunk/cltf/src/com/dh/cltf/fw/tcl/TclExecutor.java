package com.dh.cltf.fw.tcl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tcl.lang.Interp;
import tcl.lang.TCL;
import tcl.lang.TclEvent;
import tcl.lang.TclException;
import tcl.lang.TclObject;

public class TclExecutor {
	private static final Log LOG = LogFactory.getLog(TclExecutor.class);
	
	private String name;
	
	
	/** TCL interpreter */
	private Interp tclInterpreter;


	/** Thread to run the TCL interperter's event processor */
	protected TclInterpreterThread tclInterpThread;
	
	
	/**
	 * Constructor. Create and start the interpreter's process thread.
	 * @param name executor name.
	 */
	protected TclExecutor(String name) {
		this.name = name;
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
				LOG.debug("Waiting tcl process thread [" + name + "] starting.");
			} catch (InterruptedException e) {
				LOG.warn(e);
			}
		}
		tclInterpreter = tclInterpThread.getInterp();
	}
	
	/**
	 * Is TCL interpreter closed.
	 * 
	 * @return is TCL interpreter closed.
	 */
	public boolean isTclInterpClosed() {
		return ! tclInterpThread.isRunning();
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
                } catch (TclException ex) {
                    LOG.warn(ex);
                } finally {
                	TclObject tclResult = tclInterpreter.getResult();
                	if (tclResult != null) {
                		result.append(tclResult.toString());
                	}
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
                	LOG.debug("tcl(in)>" + command);
            		tclInterpreter.eval(command);
                } catch (TclException ex) {
                    //LOG.warn(ex + "  ---  " + ex.getMessage());
                } finally {
                	TclObject tclResult = tclInterpreter.getResult();
            		if ( tclResult != null ) {
            			result.append(tclResult.toString());
            		}
                }
                return 1;
	    }
        };

        // Add event to TCL Event Queue in other thread
        tclInterpreter.getNotifier().queueEvent(event, TCL.QUEUE_TAIL);

        // Wait for event to be processed by the other thread.
        event.sync();

        LOG.debug("tcl(out)>" + result);
        return result.toString();
	}
	
	
	protected Interp getTclInterpreter() {
		return tclInterpreter;
	}
	
	
	/**
	 * Get the TCL executor's name.
	 * @return name
	 */
	public String getName() {
		return name;
	}

}
