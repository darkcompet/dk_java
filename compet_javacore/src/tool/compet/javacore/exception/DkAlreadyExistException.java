package tool.compet.javacore.exception;

public class DkAlreadyExistException extends Exception {
   public DkAlreadyExistException() {
   }

   public DkAlreadyExistException(String msg) {
      super(msg);
   }

   public DkAlreadyExistException(Throwable t) {
      super(t);
   }
}
