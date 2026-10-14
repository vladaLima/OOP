package service;

import model.Route;
import model.Stop;

public record TransferRouteOption(Route firstRoute, Route secondRoute, Stop transferStop) {
    public TransferRouteOption {
        if (firstRoute == null || secondRoute == null) {
            throw new IllegalArgumentException("Routes cannot be null");
        }
        if (transferStop == null) {
            throw new IllegalArgumentException("Transfer stop cannot be null");
        }
        if (!firstRoute.containsStop(transferStop) || !secondRoute.containsStop(transferStop)) {
            throw new IllegalArgumentException("Transfer stop must belong to both routes");
        }
    }
}
