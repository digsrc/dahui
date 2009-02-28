package com.dh.cltf.fw.net;

public interface IReceiverListener {

	public void processResponseData(String message);
	
	public void processResponseData(char ch);
	
}
