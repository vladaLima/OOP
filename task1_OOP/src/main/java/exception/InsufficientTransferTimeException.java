package exception;

import java.time.Duration;

public class InsufficientTransferTimeException extends TransportException {
    public InsufficientTransferTimeException(Duration available, Duration required) {
        super("Insufficient transfer time: have " + available.toMinutes() +
                " min, but need " + required.toMinutes() + " min");
    }
}