package com.dh.cltf.fw.net;

public interface IResponseListener {

	public void processResponseData(String message);
	
	public void processResponseData(char ch);
	
}
