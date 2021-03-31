package tool.compet.core.log;

import javax.swing.*;

public class DkGuiLogger extends DkLogger {
	private static DkGuiLogger INS;

	@Override
	protected void logActual(String type, String msg) {
		JOptionPane.showConfirmDialog(null, msg, type, JOptionPane.DEFAULT_OPTION);
	}

	private DkGuiLogger() {
	}

	public static DkGuiLogger getIns() {
		if (INS == null) {
			synchronized (DkGuiLogger.class) {
				if (INS == null) {
					INS = new DkGuiLogger();
				}
			}
		}
		return INS;
	}
}
