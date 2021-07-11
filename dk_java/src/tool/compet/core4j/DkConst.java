package tool.compet.core4j;

import java.io.File;

public interface DkConst {
	// Unique object, we can use it as not-found value, data not set value...
	Object UID_OBJ = new Object();

	/**
	 * Useful constant.
	 */
	String LS = System.getProperty("line.separator");
	String FS = File.separator; // file path separator: / (unix, macos...) or \\ (windows...)
	String EMPTY_STRING = "";
	char SPACE_CHAR = ' '; // 1 byte

	/**
	 * language/country code.
	 */
	String LANG_VIETNAM = "vi";
	String LANG_ENGLISH = "en";
	String LANG_JAPAN = "ja";
	String COUNTRY_VIETNAM = "VN";
	String COUNTRY_ENGLISH = "US";
	String COUNTRY_JAPAN = "JP";
}
