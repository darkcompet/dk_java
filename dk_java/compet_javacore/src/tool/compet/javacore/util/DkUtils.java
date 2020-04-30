package tool.compet.javacore.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DkUtils {
   /**
    * @param command like "php artisan migrate"
    * @param utf8Result to store result when this command run, can be null.
    * @exception Exception when cannot detect OS type
    */
   public static void execCommand(String command, List<String> utf8Result) throws Exception {
      ProcessBuilder processBuilder = new ProcessBuilder();

      String OS = System.getProperty("os.name").toLowerCase();

      if (OS.contains("win")) {
         processBuilder.command("cmd.exe", "/c", command);
      }
      else if (OS.contains("mac")) {
         processBuilder.command("bash", "-c", command);
      }
      else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
         processBuilder.command("bash", "-c", command);
      }
      else if (OS.contains("sunos")) {
         processBuilder.command("bash", "-c", command);
      }
      else {
         throw new Exception("Could not detect OS type");
      }

      Process process = processBuilder.start();

      if (utf8Result != null) {
         String line;
         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

         while ((line = reader.readLine()) != null) {
            utf8Result.add(line);
         }
      }
   }

   /**
    * @throws RuntimeException any
    */
   public static void complain(String format, Object... args) {
      throw new RuntimeException(DkStrings.format(format, args));
   }

   /**
    * Throw RuntimeException.
    *
    * @param where nullable
    */
   public static void complain(Object where, String format, Object... args) {
      throw new RuntimeException(
         DkStrings.format("[Error] (%s)~ ", where.getClass().getName()) + DkStrings.format(format, args)
      );
   }
}
