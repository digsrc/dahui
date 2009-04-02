package com.dh.cltf.fw.device;


/**
 * 
 * @author Dahui
 *
 */
public interface ITelnetServer {

	public String getLoginTip();
	
	public String getPasswordTip();
	
	public String getEncoding();
	
	public String[] getCommandLineTips();
	
}
