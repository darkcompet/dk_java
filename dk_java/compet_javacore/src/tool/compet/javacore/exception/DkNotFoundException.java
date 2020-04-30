package tool.compet.javacore.exception;

public class DkNotFoundException extends Exception {
   public DkNotFoundException() {
   }

   public DkNotFoundException(String msg) {
      super(msg);
   }

   public DkNotFoundException(Throwable t) {
      super(t);
   }
}
