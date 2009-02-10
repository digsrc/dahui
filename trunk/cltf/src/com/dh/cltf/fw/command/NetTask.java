package com.dh.cltf.fw.command;

import java.util.concurrent.Callable;

public class NetTask implements Callable<String> {
	
	private ICommand command;
	
	private NetCommandExecutor netTaskExecutor;
	
	public NetTask(ICommand command, NetCommandExecutor netTaskExecutor) {
		this.command = command;
	}



	public String call() throws Exception {
		return null;
	}



	public ICommand getCommand() {
		return command;
	}



	public void setCommand(ICommand command) {
		this.command = command;
	}

}
