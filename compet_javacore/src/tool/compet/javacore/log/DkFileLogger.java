package tool.compet.javacore.log;

import tool.compet.javacore.constant.DkConstant;
import tool.compet.javacore.util.DkFiles;

import java.io.BufferedWriter;
import java.io.File;

public class DkFileLogger {
   private String logFilePath;

   public DkFileLogger() {
      logFilePath = new File("").getAbsolutePath() + File.separator + "log.txt";
   }

   public DkFileLogger(String logFilePath) {
      this.logFilePath = logFilePath;
   }

   public void setLogFilePath(String newLogFilePath) {
      this.logFilePath = newLogFilePath;
   }

   public boolean append(String text) {
      return doLog(text, true);
   }

   public boolean write(String text) {
      return doLog(text, false);
   }

   private boolean doLog(String text, boolean append) {
      File file = new File(logFilePath);

      try {
         DkFiles.createNewFileRecursively(file);
      }
      catch (Exception e) {
         return false;
      }

      try {
         BufferedWriter bw = DkFiles.newUtf8Writer(file);

         if (append) {
            bw.append(text);
         }
         else {
            bw.write(text);
         }

         bw.append(DkConstant.LS);
         bw.close();
      }
      catch (Exception e) {
         return false;
      }

      return true;
   }
}
