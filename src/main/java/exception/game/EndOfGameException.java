package exception.game;

public class EndOfGameException extends Exception {

    public EndOfGameException() {
    }

    public EndOfGameException(String message) {
        super(message);
    }

    public EndOfGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public EndOfGameException(Throwable cause) {
        super(cause);
    }

    public EndOfGameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
