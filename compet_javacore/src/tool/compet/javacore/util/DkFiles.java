/*
 * Copyright (c) 2018 DarkCompet. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tool.compet.javacore.util;

import tool.compet.javacore.constant.DkConstant;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class, provides common basic operations on file, directory.
 */
public class DkFiles {
	/**
	 * @param file file
	 * @return true if and only if new file was created, false if file was existed.
	 * @throws Exception if could not create new file.
	 */
	public static boolean createNewFileRecursively(File file) throws Exception {
		if (file.exists()) {
			return false;
		}

		File parent = file.getParentFile();

		if (! parent.exists() && ! parent.mkdirs()) {
			throw new Exception("Could not create new directory: " + parent.getPath());
		}

		if (file.createNewFile()) {
			return true;
		}

		throw new Exception("Could not create new target: " + file.getPath());
	}

	/**
	 * @param dir directory
	 * @return true if and only if new dir was created, false if dir was existed.
	 * @throws Exception if could not create new dir.
	 */
	public static boolean createNewDirRecursively(File dir) throws Exception {
		if (dir.exists()) {
			return false;
		}

		File parent = dir.getParentFile();

		if (! parent.exists() && ! parent.mkdirs()) {
			throw new Exception("Could not create new directory: " + parent.getPath());
		}

		if (dir.mkdir()) {
			return true;
		}

		throw new Exception("Could not create new target: " + dir.getPath());
	}

	/**
	 * Delete file or directory. Note that, Java does not delete dirty folder,
	 * so we will delete recursively children folders first.
	 *
	 * @return true if and only if file was existed and deleted successful. Otherwise exception is caused.
	 *
	 * @exception Exception if could not delete file.
	 */
	public static boolean deleteRecursively(File target) throws Exception {
		if (! target.exists()) {
			return false;
		}

		if (target.isFile()) {
			if (target.delete()) {
				return true;
			}
			throw new Exception("Could not delete file: " + target.getPath());
		}
		else {
			// delete sub dirs
			File[] children = target.listFiles();

			if (children != null) {
				for (File child : children) {
					deleteRecursively(child);
				}
			}

			// delete target itself
			if (target.delete()) {
				return true;
			}

			throw new Exception("Could not delete directory: " + target.getPath());
		}
	}

	public static void save(byte[] data, String filePath, boolean append) throws Exception {
		createNewFileRecursively(new File(filePath));

		OutputStream os = new FileOutputStream(filePath, append);
		os.write(data);
		os.close();
	}

	public static void save(String data, String filePath, boolean append) throws Exception {
		save(data == null ? "".getBytes() : data.getBytes(), filePath, append);
	}

	public static String loadAsString(String filePath) throws Exception {
		createNewFileRecursively(new File(filePath));

		String line;
		String ls = DkConstant.LS;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(filePath));

		while ((line = br.readLine()) != null) {
			sb.append(line).append(ls);
		}

		br.close();

		return sb.toString();
	}

	public static BufferedReader newUtf8Reader(File file) throws Exception {
		return new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
	}

	public static BufferedWriter newUtf8Writer(File file) throws Exception {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
	}

	public static List<File> collectFilesRecursively(File dir) {
		List<File> result = new ArrayList<>();
		File[] chilren = dir.listFiles();

		if (chilren != null) {
			for (File child : chilren) {
				if (child.isFile()) {
					result.add(child);
				}
				else {
					result.addAll(collectFilesRecursively(child));
				}
			}
		}

		return result;
	}

	/**
	 * @param file a file, eg,. welcome.bk.txt
	 * @return name of this file without extension, eg,. welcome.bk
	 */
	public static String calcFileNameWithoutExtension(File file) {
		String name = file.getName();
		int endIndex = name.lastIndexOf(".");

		return endIndex < 0 ? name : name.substring(0, endIndex);
	}

	public static List<String> readFileAsUtf8Lines(File file) throws Exception {
		List<String> lines = new ArrayList<>();
		BufferedReader reader = newUtf8Reader(file);
		String readLine;

		while ((readLine = reader.readLine()) != null) {
			lines.add(readLine);
		}

		reader.close();

		return lines;
	}
}
