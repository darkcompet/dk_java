/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core.log;

public class DkFileLogger extends DkLogger {
	@Override
	protected void logActual(String type, String msg) {
	}

	private String logFilePath;

	public DkFileLogger(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
}
