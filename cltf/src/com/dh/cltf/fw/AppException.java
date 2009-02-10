package com.dh.cltf.fw;

public class AppException extends Exception {

	private static final long serialVersionUID = 2321293096484364311L;

	public AppException() {
		super();
	}
	
	public AppException(String msg) {
		super(msg);
	}
	
    public AppException(String s, Throwable t) {
        super(s, t);
    }

    public AppException(Throwable t) {
        super(t);
    } 
}
