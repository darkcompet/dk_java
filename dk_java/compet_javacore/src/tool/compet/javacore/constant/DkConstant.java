package tool.compet.javacore.constant;

import java.io.File;

public interface DkConstant {
   /** System line feed */
   String LS = System.lineSeparator();

   /** Full path to current running app */
   String ABS_PATH = new File("").getAbsolutePath();

   /** Date format pattern */
   String DATE_FORMAT_US = "yyyy/MM/dd HH:mm:ss SSS";
   String DATE_FORMAT_JA = "yyyy年MM月dd日 HH時mm分ss秒 SSS";
   String DATE_FORMAT_VI = "Ngày dd/MM/yyyy HH:mm:ss SSS";
}
