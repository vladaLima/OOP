package exception;

import model.Stop;

public class RouteNotFoundException extends TransportException {
    public RouteNotFoundException(Stop from, Stop to) {
        super("No direct route found between " + from.StopName() + " and " + to.StopName());
    }
}