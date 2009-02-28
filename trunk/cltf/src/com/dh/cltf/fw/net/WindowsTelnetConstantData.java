package com.dh.cltf.fw.net;


/**
 * This class's data field is the constant data of the windows telnet server's response.
 * The parser class will use these data as the keyword or separator to parse the telnet server's response.
 * 
 * @author Dahui
 *
 */
public class WindowsTelnetConstantData {

	//
	public static final String LOGIN_USERNAME_TIP = "login: ";
	public static final String LOGIN_PASSWORD_TIP = "Password: ";
	
	public static final String TIMEOUT_TIP = "Session timed out.";
	public static final String SERVER_CLOSE_TIP = "Telnet Server has closed the connection";
	
	// welcome string can be get when the telnet client connect to server. 
	public static String LOGIN_WELCOME_TIP = "";
	
	
	
}
