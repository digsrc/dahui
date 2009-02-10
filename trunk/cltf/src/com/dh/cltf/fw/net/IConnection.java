package com.dh.cltf.fw.net;

import com.dh.cltf.fw.AppException;

public interface IConnection {
	
	public void connect() throws AppException;
	
	public void disconnect() throws AppException;
	
	public void send(String message) throws AppException;
	
	public String receiveText()  throws AppException;
	
}
