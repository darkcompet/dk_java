package tool.compet.core.log;

import androidx.annotation.Nullable;
import tool.compet.core.constant.DkConst;
import tool.compet.core.util.DkStrings;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;

public abstract class DkLogger {
	protected abstract void logActual(String type, String msg);

	public static final String RAW = "RAW";
	public static final String DEBUG = "DEBUG";
	public static final String INFO = "INFO";
	public static final String WARNING = "WARNING";
	public static final String ERROR = "ERROR";

	private final ThreadLocal<SimpleDateFormat> dateFormater = ThreadLocal.withInitial(() ->
		new SimpleDateFormat(DkConst.DATE_FORMAT)
	);

	// For benchmark
	private long benchmarkStartTime;
	private ArrayDeque<String> benchmarkTaskNames;

	public void debug(@Nullable Object where, String format, Object... args) {
		logActual(DEBUG, makeMessage(DEBUG, where, format, args));
	}

	public void info(@Nullable Object where, String format, Object... args) {
		logActual(INFO, makeMessage(INFO, where, format, args));
	}

	public void warning(@Nullable Object where, String format, Object... args) {
		logActual(WARNING, makeMessage(WARNING, where, format, args));
	}

	public void error(@Nullable Object where, String format, Object... args) {
		logActual(ERROR, makeMessage(ERROR, where, format, args));
	}

	public void error(@Nullable Object where, Throwable e) {
		error(where, e, null);
		e.printStackTrace();
	}

	public void error(@Nullable Object where, Throwable e, String format, Object... args) {
		logActual(ERROR, makeThrowableMessage(where, e, format, args));
	}

	/**
	 * It is like as `pen` down.
	 */
	public void tick(@Nullable Object where, String task) {
		if (benchmarkTaskNames == null) {
			benchmarkTaskNames = new ArrayDeque<>();
		}

		benchmarkTaskNames.push(task);
		debug(where, "[%s] was started", task);
		benchmarkStartTime = System.currentTimeMillis();
	}

	/**
	 * It is like as `pen` up.
	 */
	public void tock(@Nullable Object where) {
		long elapsed = System.currentTimeMillis() - benchmarkStartTime;

		debug(where, "[%s] end in: %d s %d ms", benchmarkTaskNames.pop(), elapsed / 1000, (elapsed - 1000 * (elapsed / 1000)));
	}

	//
	// Protected region
	//

	protected String makePrefix(String type, Object where) {
		String time = dateFormater.get().format(new Date());

		if (where != null) {
			String loc = where instanceof Class ? ((Class) where).getName() : where.getClass().getName();
			return DkStrings.format("%s [%s] (%s)~ ", time, type, loc.substring(loc.lastIndexOf('.') + 1));
		}
		return DkStrings.format("%s [%s]~ ", time, type);
	}

	protected String makeMessage(String type, Object where, String format, Object... args) {
		if (args != null && args.length > 0) {
			format = DkStrings.format(format, args);
		}
		return makePrefix(type, where) + format;
	}

	protected String makeThrowableMessage(Object where, Throwable e, String format, Object... args) {
		StringBuilder sb = new StringBuilder();

		if (format != null) {
			if (args != null) {
				format = DkStrings.format(format, args);
			}
			sb.append("Message: ").append(format).append(DkConst.LS);
		}

		sb.append(e.toString()).append(DkConst.LS);

		for (StackTraceElement traceElement : e.getStackTrace()) {
			sb.append("\tat ").append(traceElement).append(DkConst.LS);
		}

		return makeMessage(ERROR, where, sb.toString());
	}
}
