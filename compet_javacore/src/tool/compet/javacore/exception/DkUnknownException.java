package tool.compet.javacore.exception;

public class DkUnknownException extends Exception {
   public DkUnknownException() {
   }

   public DkUnknownException(String msg) {
      super(msg);
   }

   public DkUnknownException(Throwable t) {
      super(t);
   }
}
