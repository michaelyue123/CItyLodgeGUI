package model.customexception;

public class UnableReturnException extends Exception {

    public UnableReturnException() {
        super("Unable to return room when the room status is not under rented!");
    }
}
