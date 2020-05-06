package model.customexception;

public class UnableMaintainException extends Exception {

    public UnableMaintainException() {
        super("Cannot perform maintenance under rented status!");
    }
}
