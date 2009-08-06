package com.dh.cltf.fw.script;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dh.cltf.fw.device.IDevice;


public class RemoteCommand extends TagSupport {
	private static final Log Log = LogFactory.getLog(RemoteCommand.class);
	
	
	private IDevice targetDevice;
	
	private String command;
	
	private String result;
	
	public RemoteCommand() {
		
	}

	
    public void run() {
    	Log.debug("RemoteCommand,  command=" + command);
    }
    
    public void doTag(XMLOutput output) {
        try {
			getBody().run( context, output );
		} catch (JellyTagException e) {
			Log.error(e.getMessage());
		}
    }

    
	public IDevice getTargetDevice() {
		return targetDevice;
	}


	public void setTargetDevice(IDevice targetDevice) {
		this.targetDevice = targetDevice;
	}


	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}
