package mealPlanner.exception;

public class NoSuchFileTypeException extends RuntimeException {
    public NoSuchFileTypeException(String unhandledFileType) {
        super(unhandledFileType);
    }
}
