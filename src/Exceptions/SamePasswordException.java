package Exceptions;

public class SamePasswordException extends Exception {
    public SamePasswordException(String message) {
        super(message);
    }
}
