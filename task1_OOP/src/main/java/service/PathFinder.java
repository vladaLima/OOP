package service;

import exception.TransportException;
import model.Route;
import model.Stop;

import java.util.List;

public interface PathFinder {
    List<Route> findRoutes(Stop from, Stop to) throws TransportException;
}
