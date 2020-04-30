package tool.compet.javacore.log;

import tool.compet.javacore.constant.DkConstant;
import tool.compet.javacore.util.DkStrings;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.concurrent.ThreadFactory;

public abstract class Logger {
   protected abstract void log(String type, String msg);

   public static final String PURE = "PURE";
   public static final String DEBUG = "DEBUG";
   public static final String INFO = "INFO";
   public static final String WARN = "WARN";
   public static final String ERROR = "ERROR";

   private final ThreadLocal<SimpleDateFormat> dateFormater = ThreadLocal.withInitial(() ->
      new SimpleDateFormat(DkConstant.DATE_FORMAT_VI)
   );

   // For benchmark
   private long benchmarkStartTime;
   private ArrayDeque<String> benchmarkTaskNames;

   private String makePrefix(String type, Object where) {
      String time = dateFormater.get().format(new Date());

      if (where != null) {
         String loc = where instanceof Class ? ((Class) where).getName() : where.getClass().getName();
         return DkStrings.format("%s [%s] (%s)~ ", time, type, loc.substring(loc.lastIndexOf('.') + 1));
      }
      return DkStrings.format("%s [%s]~ ", time, type);
   }

   private String makeMessage(String type, Object where, String format, Object... args) {
      if (args != null && args.length > 0) {
         format = DkStrings.format(format, args);
      }
      return makePrefix(type, where) + format;
   }

   /**
    * @param where nullable
    */
   public void debug(Object where, String format, Object... args) {
      log(DEBUG, makeMessage(DEBUG, where, format, args));
   }

   /**
    * @param where nullable
    */
   public void info(Object where, String format, Object... args) {
      log(INFO, makeMessage(INFO, where, format, args));
   }

   /**
    * @param where nullable
    */
   public void warn(Object where, String format, Object... args) {
      log(WARN, makeMessage(WARN, where, format, args));
   }

   public void error(Object where, String format, Object... args) {
      log(ERROR, makeMessage(ERROR, where, format, args));
   }

   /**
    * @param where nullable
    */
   public void error(Object where, Throwable e) {
      error(where, e, null);
      e.printStackTrace();
   }

   /**
    * @param where nullable
    */
   public void error(Object where, Throwable e, String format, Object... args) {
      log(ERROR, makeThrowableMessage(where, e, format, args));
   }

   private String makeThrowableMessage(Object where, Throwable e, String format, Object... args) {
      String ls = DkConstant.LS;
      StringBuilder sb = new StringBuilder();

      if (format != null) {
         if (args != null) {
            format = DkStrings.format(format, args);
         }
         sb.append("Message: ").append(format).append(ls);
      }

      sb.append(e.toString()).append(ls);

      for (StackTraceElement traceElement : e.getStackTrace()) {
         sb.append("\tat ").append(traceElement).append(ls);
      }

      return makeMessage(ERROR, where, sb.toString());
   }

   /**
    * @param where nullable
    */
   public void tick(Object where, String task) {
      if (benchmarkTaskNames == null) {
         benchmarkTaskNames = new ArrayDeque<>();
      }

      benchmarkTaskNames.push(task);
      debug(where, "[%s] was started", task);
      benchmarkStartTime = System.currentTimeMillis();
   }

   /**
    * @param where nullable
    */
   public void tock(Object where) {
      long elapsed = System.currentTimeMillis() - benchmarkStartTime;

      debug(where, "[%s] end in: %d s %d ms",
         benchmarkTaskNames.pop(), elapsed / 1000, (elapsed - 1000 * (elapsed / 1000)));
   }
}
