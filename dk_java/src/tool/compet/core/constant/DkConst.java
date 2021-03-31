/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.core.constant;

import java.io.File;

public interface DkConst {
	// Separator
	String LS = System.lineSeparator();
	String LS_REGEX = "\\r\\n|\\n|\\r"; // Regex of line separator
	String FS = File.separator;

	String ABS_PATH = new File("").getAbsolutePath();

	String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";

	String EMPTY_STRING = "";

	// language/country code
	String LANG_VIETNAM = "vi";
	String COUNTRY_VIETNAM = "VN";
	String LANG_ENGLISH = "en";
	String COUNTRY_ENGLISH = "US";
	String LANG_JAPAN = "ja";
	String COUNTRY_JAPAN = "JP";
}
