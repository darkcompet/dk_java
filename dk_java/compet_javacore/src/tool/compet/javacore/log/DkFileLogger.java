package tool.compet.javacore.log;

import tool.compet.javacore.constant.DkConstant;
import tool.compet.javacore.util.DkFiles;
import tool.compet.javacore.util.DkStrings;

import java.io.BufferedWriter;
import java.io.File;

public class DkFileLogger {
   private final Logger logger = new Logger() {
      @Override
      protected void log(String type, String msg) {
         try {
            File file = new File(logFilePath);
            DkFiles.createNewFileRecursively(file);

            BufferedWriter bw = DkFiles.newUtf8Writer(file, true);
            bw.write(msg);
            bw.write(DkConstant.LS);
            bw.close();
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
   };

   private String logFilePath;

   public DkFileLogger() {
      this.logFilePath = DkConstant.ABS_PATH + "/log.txt";
   }

   public DkFileLogger(String logFilePath) {
      this.logFilePath = logFilePath;
   }

   public void setLogFilePath(String newLogFilePath) {
      this.logFilePath = newLogFilePath;
   }

   /** Just log. No customization is applied by this class */
   public void justLog(String format, Object... args) {
      logger.log(Logger.PURE, DkStrings.format(format, args));
   }

   public void debug(Object where, String format, Object... args) {
      logger.debug(where, format, args);
   }

   public void info(Object where, String format, Object... args) {
      logger.info(where, format, args);
   }

   public void warn(Object where, String format, Object... args) {
      logger.warn(where, format, args);
   }

   /**
    * @param where nullable
    */
   public void error(Object where, Throwable e) {
      logger.error(where, e);
   }

   public void error(Object where, String format, Object... args) {
      logger.error(where, format, args);
   }

   public void error(Object where, Throwable e, String format, Object... args) {
      logger.error(where, e, format, args);
   }
}
