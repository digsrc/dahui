package com.dh.cltf.fw.net;

import com.dh.cltf.fw.AppException;


/**
 * Represent a network connection.
 * 
 * @author Dahui
 *
 */
public interface IConnection {
	
	public void open() throws AppException;
	
	public void close() throws AppException;
	
	/**
	 * To send a text message. To send a byte[] maybe more common, but in this project
	 * has no this kind of requirement. 
	 * 
	 * @param message	message to send.
	 * @throws AppException
	 */
	public void send(String message) throws AppException;
	
	public String receive()  throws AppException;
	
	
	/**
	 * Add a listener to process the data which this connection receives.
	 */
	public void addReceiverLisetner(IReceiverListener receiverListern);
}
