package exception;

public class InvalidRomanNumberException extends RuntimeException {
    public InvalidRomanNumberException(String s) {
        super(s);
    }
}
