package service;

import exception.RouteNotFoundException;
import exception.TransportException;
import model.Route;
import model.Stop;

import java.util.List;

public class DirectPathFinder implements PathFinder {
    private final List<Route> allRoutes;

    public DirectPathFinder(List<Route> allRoutes) {
        if (allRoutes == null) {
            throw new IllegalArgumentException("Routes cannot be null");
        }

        this.allRoutes = List.copyOf(allRoutes);
    }

    @Override
    public List<Route> findRoutes(Stop from, Stop to) throws TransportException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Stops cannot be null");
        }

        List<Route> directRoutes = allRoutes.stream().filter(route -> route.canTravel(from, to)).toList();

        if (directRoutes.isEmpty()) {
            throw new RouteNotFoundException(from, to);
        }

        return directRoutes;
    }
}
