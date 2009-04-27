package com.dh.cltf.fw.tcl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tcl.lang.Interp;
import tcl.lang.TCL;

/**
 * This thread is used as the TCL interpreter event processor.
 * All the TCL command is send as a event and process by this thread.
 * 
 * Each TclExecutor has an instance of this class and run as a separate thread. 
 * So, it means that each tclExecutor has its own TCL interpreter.
 * 
 * @author Yuan Dahui.
 *
 */
public class TclInterpreterThread extends Thread {
	private static final Log LOG = LogFactory.getLog(TclInterpreterThread.class);
			
	/** interpreter */
    private Interp interp;
    
    /** stop flag */
    private volatile boolean isStop = false;
    
    /** running flag */
    private volatile boolean isRunning = false;
    
    /**
     * run method.
     */
    public void run() {
        interp = new Interp();
        try {
        	isRunning = true;
        	
            while (!isStop && !Thread.interrupted()) {
                interp.getNotifier().doOneEvent(TCL.ALL_EVENTS);
            }
        } catch (Exception e) {
        	LOG.warn(e);
        	LOG.warn("TCL interpreter thread is stopped by exception: " + e.getMessage());
        } finally { 
        	LOG.debug("TCL interpreter thread is stoped.    isStop:" + isStop);
            interp.dispose();
        }
        
        isRunning = false;
    }
    
    
    /**
     * Set stop flag.
     * 
     * @param isStop to stop if true
     */
    public void setStop(boolean isStop) {
    	this.isStop = isStop;
    }
    
    /**
     * Get interpreter.
     * 
     * @return TCL interpreter
     */
	public Interp getInterp() {
		return interp;
	}


	/**
	 * Is running.
	 * 
	 * @return isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}
}
