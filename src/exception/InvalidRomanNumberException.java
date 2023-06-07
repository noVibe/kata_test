package exception;

public class InvalidRomanNumberException extends RuntimeException {
    public InvalidRomanNumberException() {
        super("Impossible Roman number.");
    }
}
