package junit.tutorial.employee.exception;

public class UnfairSalaryException extends RuntimeException {
    public UnfairSalaryException(String message) {
        super(message);
    }
}
