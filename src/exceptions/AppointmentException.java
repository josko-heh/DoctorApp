package exceptions;

public class AppointmentException extends Exception {
    private static final long serialVersionUID = 1809782090670566414L;

    public AppointmentException() {
    }

    public AppointmentException(String message) {
        super(message);
    }

    public AppointmentException(Throwable cause) {
        super(cause);
    }

    public AppointmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
