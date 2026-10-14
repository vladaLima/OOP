package exception;

import model.Stop;

public class RouteNotFoundException extends TransportException {
    public RouteNotFoundException(Stop from, Stop to) {
        super("No direct route found between " + from.name() + " and " + to.name());
    }
    public RouteNotFoundException(String message) {
        super(message);
    }
}