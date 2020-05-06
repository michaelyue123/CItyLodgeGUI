package model.customexception;

public class NegativeDayDifferenceException extends Exception {

    public NegativeDayDifferenceException() {
        super("Day difference cannot be negative!");
    }
}
