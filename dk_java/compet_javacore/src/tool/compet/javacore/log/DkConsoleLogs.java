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

package tool.compet.javacore.log;

import tool.compet.javacore.util.DkStrings;

/**
 * Logging utility class. It is useful for console and vision logging, and performance benchmark...
 */
public class DkConsoleLogs {
   private static final Logger logger = new Logger() {
      @Override
      protected void log(String type, String msg) {
         switch (type) {
            case PURE:
            case DEBUG:
            case INFO: {
               System.out.println(msg);
               break;
            }
            case WARN:
            case ERROR: {
               System.err.println(msg);
               break;
            }
         }
      }
   };

   /** Just log. No customization is applied by this class */
   public static void justLog(String format, Object... args) {
      logger.log(Logger.PURE, DkStrings.format(format, args));
   }

	/**
	 * @param where nullable
	 */
	public static void debug(Object where, String format, Object... args) {
		logger.debug(where, format, args);
	}

	/**
	 * @param where nullable
	 */
	public static void info(Object where, String format, Object... args) {
      logger.info(where, format, args);
	}

	/**
	 * @param where nullable
	 */
	public static void warn(Object where, String format, Object... args) {
      logger.warn(where, format, args);
	}

   public static void error(Object where, String format, Object... args) {
      logger.error(where, format, args);
   }

	/**
	 * @param where nullable
	 */
	public static void error(Object where, Throwable e) {
		logger.error(where, e);
	}

	/**
	 * @param where nullable
	 */
	public static void error(Object where, Throwable e, String format, Object... args) {
      logger.error(where, e, format, args);
	}
}
