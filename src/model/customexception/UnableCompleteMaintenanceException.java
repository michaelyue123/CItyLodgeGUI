package model.customexception;

public class UnableCompleteMaintenanceException extends Exception {

    public UnableCompleteMaintenanceException() {
        super("Unable to complete maintenance when room status is not under maintenance!");
    }
}
