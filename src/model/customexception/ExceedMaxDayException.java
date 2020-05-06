package model.customexception;

public class ExceedMaxDayException extends Exception {

    public ExceedMaxDayException() {
        super("The number of rent day cannot be larger than number of days can be rented!");
    }
}
