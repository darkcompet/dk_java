/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.core.util;

import androidx.annotation.Nullable;
import tool.compet.core.BuildConfig;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Console log.
 */
public class DkLogs {
	// Enable it to log backtrace
	public static boolean logBackTrace = true;

	// Log types
	private static final String DEBUG = "DEBUG";
	private static final String INFO = "INFO";
	private static final String WARNING = "WARNING";
	private static final String ERROR = "ERROR";

	// For benchmark
	private static long benchmarkStartTime;
	private static ArrayDeque<String> benchmarkTaskNames;

	/**
	 * Debug log. Can't be invoked in production.
	 * Note that, we should remove all debug code when release.
	 */
	public static void debug(Object where, String format, Object... args) {
		log(false, DEBUG, where, format, args);
	}

	/**
	 * Log info. Can be invoked in production.
	 * <p>
	 * If sometime caller wanna log it only in local env, so caller can
	 * wrap this function with DEBUG constant instead of call it directly.
	 */
	public static void info(Object where, String format, Object... args) {
		log(true, INFO, where, format, args);
	}

	/**
	 * Warning log. Can be invoked in production.
	 */
	public static void warning(Object where, String format, Object... args) {
		log(true, WARNING, where, format, args);
	}

	/**
	 * Error log. Can be invoked in production.
	 */
	public static void error(Object where, String format, Object... args) {
		log(true, ERROR, where, format, args);
	}

	/**
	 * Exception log. Can be invoked in production.
	 */
	public static void error(Object where, Throwable e) {
		error(where, e.getMessage());
	}

	/**
	 * Start benchmark. Can't be invoked in production.
	 */
	public static void tick(@Nullable Object where, String task) {
		if (benchmarkTaskNames == null) {
			benchmarkTaskNames = new ArrayDeque<>();
		}

		benchmarkTaskNames.push(task);
		log(false, DEBUG, where, "Task [%s] was started", task);
		benchmarkStartTime = System.currentTimeMillis();
	}

	/**
	 * End benchmark. Can't be invoked in production.
	 */
	public static void tock(@Nullable Object where) {
		long elapsed = System.currentTimeMillis() - benchmarkStartTime;
		log(false, DEBUG, where,
			"Task [%s] end in: %d.%3d s",
			benchmarkTaskNames.pop(),
			elapsed / 1000,
			(elapsed - 1000 * (elapsed / 1000)));
	}

	private static void log(boolean validAtProduction, String logType, Object where, String format, Object... args) {
		String message = format;
		if (args != null && args.length > 0) {
			message = DkStrings.format(format, args);
		}

		message = makePrefix(logType, where) + message;

		logActual(validAtProduction, logType, message);
	}

	private static void logActual(boolean validAtProduction, String logType, String message) {
		if (! BuildConfig.DEBUG && ! validAtProduction) {
			DkUtils.complainAt(DkLogs.class, "Can not use log type %d in product version. You maybe need wrap it in DEBUG constant.", logType);
		}

		switch (logType) {
			case DEBUG:
			case INFO: {
				System.out.println(message);
				break;
			}
			case WARNING:
			case ERROR: {
				if (logBackTrace) {
					List<String> descriptions = new ArrayList<>();
					for (StackTraceElement elm : Thread.currentThread().getStackTrace()) {
						String description = DkStrings.format("%s (%d) ==> %s.%s()", elm.getFileName(), elm.getLineNumber(), elm.getClassName(), elm.getMethodName());
						descriptions.add(description);
					}
					String trace = DkStrings.join('\n', descriptions);
					message += "\nStack Trace:\n" + trace;
				}
				System.err.println(message);
				break;
			}
		}
	}

	private static String makePrefix(String logType, @Nullable Object where) {
		String prefix = "~ ";

		if (where != null) {
			String loc = where instanceof Class ? ((Class) where).getName() : where.getClass().getName();
			loc = loc.substring(loc.lastIndexOf('.') + 1);
			prefix = loc + prefix;
		}

		return "[" + logType + "] " + prefix;
	}
}
