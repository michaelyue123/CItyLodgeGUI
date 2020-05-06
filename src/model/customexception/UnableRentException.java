package model.customexception;

public class UnableRentException extends Exception {

    public UnableRentException() {
        super("This room is currently unavailable.");
    }
}
