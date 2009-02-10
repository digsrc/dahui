package com.dh.cltf.fw.command;

import java.util.concurrent.ExecutorService;


public class NetCommandExecutor {

	private ExecutorService exec;
	
	public NetCommandExecutor() {
	}
	
	public void execute(ICommand command) {
		exec.submit(command);
	}

}
