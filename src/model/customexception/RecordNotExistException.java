package model.customexception;

public class RecordNotExistException extends Exception {

    public RecordNotExistException() {
        super("This hiring record does not exist!");
    }
}
