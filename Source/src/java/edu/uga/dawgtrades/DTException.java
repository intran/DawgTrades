package edu.uga.dawgtrades;

public class DTException
        extends Exception {

    private static final long serialVersionUID = 1L;

    public DTException(String message) {
        super(message);
    }

    public DTException(Throwable cause) {
        super(cause);
    }
}
