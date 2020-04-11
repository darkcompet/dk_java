package tool.compet.javacore.log;

import tool.compet.javacore.constant.DkConstant;
import tool.compet.javacore.util.DkStrings;

import java.util.ArrayDeque;

public abstract class Logger {
   protected abstract void println(String type, String msg);

   public final String DEBUG = "DEBUG";
   public final String INFO = "INFO";
   public final String WARN = "WARN";
   public final String ERROR = "ERROR";

   // For benchmark
   private long benchmarkStartTime;
   private ArrayDeque<String> benchmarkTaskNames;

   /**
    * @param type any
    * @param where nullable
    * @return prefix
    */
   private String makePrefix(String type, Object where) {
      if (where != null) {
         String loc = where instanceof Class ? ((Class) where).getName() : where.getClass().getName();
         return DkStrings.format("[%s] (%s)~ ", type, loc.substring(loc.lastIndexOf('.') + 1));
      }
      return DkStrings.format("[%s] ", type);
   }

   private String makeMessage(String type, Object where, String format, Object... args) {
      if (args != null && args.length > 0) {
         format = DkStrings.format(format, args);
      }
      return makePrefix(type, where) + format;
   }

   /**
    * Debug log. Should Not be invoked in production.
    *
    * @param where nullable
    */
   public void debug(Object where, String format, Object... args) {
      println(DEBUG, makeMessage(DEBUG, where, format, args));
   }

   /**
    * Normal log. Should Not be called in production.
    *
    * @param where nullable
    */
   public void info(Object where, String format, Object... args) {
      println(INFO, makeMessage(INFO, where, format, args));
   }

   /**
    * Warning log. Can be invoked in production.
    *
    * @param where nullable
    */
   public void warn(Object where, String format, Object... args) {
      println(WARN, makeMessage(WARN, where, format, args));
   }

   /**
    * Exception log. Can be invoked in production.
    *
    * @param where nullable
    */
   public void error(Object where, Throwable e) {
      error(where, e, null);
      e.printStackTrace();
   }

   /**
    * Exception log. Can be invoked in production.
    *
    * @param where nullable
    */
   public void error(Object where, Throwable e, String format, Object... args) {
      println(ERROR, makeThrowableMessage(where, e, format, args));
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
    * Start benchmark. Can't be invoked in production.
    *
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
    * End benchmark. Can't be invoked in production.
    *
    * @param where nullable
    */
   public void tock(Object where) {
      long elapsed = System.currentTimeMillis() - benchmarkStartTime;

      debug(where, "[%s] end in: %d s %d ms",
         benchmarkTaskNames.pop(), elapsed / 1000, (elapsed - 1000 * (elapsed / 1000)));
   }
}
