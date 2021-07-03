/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class, provides common basic operations for app.
 */
public class DkUtils {
	/**
	 * Throw RuntimeException with given msg.
	 */
	public static void complain(String format, Object... args) {
		throw new RuntimeException(DkStrings.format(format, args));
	}

	/**
	 * Throw RuntimeException with given msg and location of class which call it.
	 */
	public static void complainAt(Object where, String format, Object... args) {
		String prefix = "~ ";
		if (where != null) {
			String loc = (where instanceof Class) ? ((Class) where).getName() : where.getClass().getName();
			loc = loc.substring(loc.lastIndexOf('.') + 1);
			prefix = loc + prefix;
		}
		throw new RuntimeException(prefix + DkStrings.format(format, args));
	}

	public static boolean sleep(long millis) {
		try {
			Thread.sleep(millis);
			return true;
		}
		catch (Exception e) {
			DkConsoleLogs.error(DkUtils.class, e);
			return false;
		}
	}

	public static void execCommand(String command) throws Exception {
		List<String> utf8Result = new ArrayList<>();
		execCommand(command, utf8Result);
	}

	/**
	 * @param command like "php artisan migrate"
	 * @throws Exception when cannot detect OS type
	 */
	public static void execCommand(String command, List<String> utf8Result) throws Exception {
		String osname = System.getProperty("os.name").toLowerCase();
		ProcessBuilder processBuilder = new ProcessBuilder();

		if (osname.contains("win")) {
			processBuilder.command("cmd.exe", "/c", command);
		}
		else if (osname.contains("mac")) {
			processBuilder.command("bash", "-c", command);
		}
		else if (osname.contains("nix") || osname.contains("nux") || osname.contains("aix")) {
			processBuilder.command("bash", "-c", command);
		}
		else if (osname.contains("sunos")) {
			processBuilder.command("bash", "-c", command);
		}
		else {
			throw new Exception("Could not detect OS type");
		}

		if (utf8Result != null) {
			java.lang.Process process = processBuilder.start();
			String line;
			Charset utf8Charset = Charset.forName("UTF-8");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), utf8Charset));

			while ((line = reader.readLine()) != null) {
				utf8Result.add(line);
			}
		}
	}

	public static String stream2string(InputStream is) {
		String line;
		String ls = DkConst.LS;
		StringBuilder sb = new StringBuilder(256);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			while ((line = br.readLine()) != null) {
				sb.append(line).append(ls);
			}
		}
		catch (Exception e) {
			DkConsoleLogs.error(DkUtils.class, e);
		}

		return sb.toString();
	}
}
