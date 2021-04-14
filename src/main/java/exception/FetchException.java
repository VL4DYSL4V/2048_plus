package exception;

public final class FetchException extends Exception{

    public FetchException(String message) {
        super(message);
    }

    public FetchException(Throwable cause) {
        super(cause);
    }

}
