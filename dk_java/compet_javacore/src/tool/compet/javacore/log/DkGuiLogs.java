package tool.compet.javacore.log;

import tool.compet.javacore.util.DkStrings;

import javax.swing.*;

public class DkGuiLogs {
   /**
    * @return an integer which indicates option selected by user.
    */
   public static int showMsgBox(String title, String format, Object... args) {
      String msg = DkStrings.format(format, args);
      return JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.DEFAULT_OPTION);
   }
}
