package service;

import exception.RouteNotFoundException;
import exception.TransportException;
import model.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class TransferPathFinder implements PathFinder {
    private final List<Trip> trips;
    private final List<Route> routes;

    public TransferPathFinder(List<Trip> trips, List<Route> routes) {
        if (trips == null) {
            throw new IllegalArgumentException("Trips cannot be null");
        }
        if (routes == null) {
            throw new IllegalArgumentException("Routes cannot be null");
        }

        this.trips = List.copyOf(trips);
        this.routes = List.copyOf(routes);
    }

    @Override
    public List<Route> findRoutes(Stop from, Stop to) throws TransportException {
        List<TransferRouteOption> options = findTransferOptions(from, to);

        if (options.isEmpty()) {
            throw new RouteNotFoundException("No route with one transfer found between " + from.name() + " and " + to.name());
        }

        Set<Route> uniqueRoutes = new LinkedHashSet<>();

        for (TransferRouteOption option : options) {
            uniqueRoutes.add(option.firstRoute());
            uniqueRoutes.add(option.secondRoute());
        }

        return List.copyOf(uniqueRoutes);
    }

    public List<TransferRouteOption> findTransferOptions(Stop from, Stop to) throws TransportException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Stops cannot be null");
        }

        List<TransferRouteOption> options = new ArrayList<>();

        for (Route firstRoute : routes) {
            if (!firstRoute.containsStop(from)) {
                continue;
            }

            for (Route secondRoute : routes) {
                if (firstRoute.equals(secondRoute)) {
                    continue;
                }
                if (!secondRoute.containsStop(to)) {
                    continue;
                }

                for (RouteSegment segment : firstRoute.getSegments()) {
                    Stop transferStop = segment.stop();
                    if (firstRoute.canTravel(from, transferStop) && secondRoute.canTravel(transferStop, to)) {
                        TransferRouteOption option = new TransferRouteOption(firstRoute, secondRoute, transferStop);
                        if (!options.contains(option)) {
                            options.add(option);
                        }
                    }
                }
            }
        }

        return options;
    }

    public TransferJourney findBestJourney(Stop from, Stop to, LocalDate travelDate, Duration minTransferTime) throws TransportException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Stops cannot be null");
        }
        if (minTransferTime == null || minTransferTime.isNegative()) {
            throw new IllegalArgumentException("Minimum transfer time cannot be null or negative");
        }
        if (travelDate == null) {
            throw new IllegalArgumentException("Travel date cannot be null");
        }

        TransferJourney bestJourney = null;
        List<TransferRouteOption> options = findTransferOptions(from, to);

        for (TransferRouteOption option : options) {
            for (Trip firstTrip : trips) {
                if (!firstTrip.getRoute().equals(option.firstRoute())) {
                    continue;
                }
                for (Trip secondTrip : trips) {
                    if (!secondTrip.getRoute().equals(option.secondRoute())) {
                        continue;
                    }

                    TransferJourney candidate = findBestForTrips(firstTrip, secondTrip, from, to, option.transferStop(), travelDate, minTransferTime);

                    if (candidate == null) {
                        continue;
                    }
                    if (bestJourney == null || candidate.totalDuration().compareTo(bestJourney.totalDuration()) < 0) {
                        bestJourney = candidate;
                    }
                }
            }

        }

        if (bestJourney == null) {
            throw new RouteNotFoundException("No suitable journey with one transfer found between " + from.name() + " and " + to.name());
        }

        return bestJourney;
    }

    private DayType getDayType(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();

        return switch (day) {
            case SATURDAY, SUNDAY -> DayType.WEEKEND;
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> DayType.WEEKDAY;
        };
    }

    private TransferJourney findBestForTrips(Trip firstTrip, Trip secondTrip, Stop from, Stop to, Stop transferStop, LocalDate travelDate, Duration minTransferTime) {
        Route firstRoute = firstTrip.getRoute();
        Route secondRoute = secondTrip.getRoute();
        TransferJourney bestJourney = null;
        for (int boardingIndex : firstRoute.indexesOfStop(from)) {
            LocalDate firstTripDate = findFirstTripDate(firstTrip, boardingIndex, travelDate);
            if (firstTrip.getDayType() != getDayType(firstTripDate)) {
                continue;
            }
            for (int firstTransferIndex : firstRoute.indexesOfStop(transferStop)) {
                if (firstTransferIndex <= boardingIndex) {
                    continue;
                }
                LocalDateTime firstTripArrival = firstTrip.getArrivalDateTimeAt(firstTripDate, firstTransferIndex);
                LocalDateTime earliestBoarding = firstTripArrival.plus(minTransferTime);
                for (int secondTransferIndex : secondRoute.indexesOfStop(transferStop)) {
                    LocalDate secondTripDate = findSecondTripDate(secondTrip, secondTransferIndex, earliestBoarding);

                    int destinationIndex = -1;

                    for (int index : secondRoute.indexesOfStop(to)) {
                        if (index > secondTransferIndex) {
                            destinationIndex = index;
                            break;
                        }
                    }

                    if (destinationIndex == -1) {
                        continue;
                    }

                    TransferJourney candidate = buildJourney(firstTrip, firstTripDate, secondTrip, secondTripDate, transferStop, boardingIndex, firstTransferIndex, secondTransferIndex, destinationIndex, minTransferTime);

                    if (candidate != null && (bestJourney == null || candidate.totalDuration().compareTo(bestJourney.totalDuration()) < 0)) {
                        bestJourney = candidate;
                    }
                }
            }
        }

        return bestJourney;
    }

    private TransferJourney buildJourney(Trip firstTrip, LocalDate firstTripDate, Trip secondTrip, LocalDate secondTripDate, Stop transferStop, int boardingIndex, int firstTransferIndex, int secondTransferIndex, int destinationIndex, Duration minTransferTime) {
        LocalDateTime firstDepartureDateTime = firstTrip.getArrivalDateTimeAt(firstTripDate, boardingIndex);
        LocalDateTime firstArrivalDateTime = firstTrip.getArrivalDateTimeAt(firstTripDate, firstTransferIndex);
        LocalDateTime secondDepartureDateTime = secondTrip.getArrivalDateTimeAt(secondTripDate, secondTransferIndex);
        LocalDateTime secondArrivalDateTime = secondTrip.getArrivalDateTimeAt(secondTripDate, destinationIndex);
        Duration waitingTime = Duration.between(firstArrivalDateTime, secondDepartureDateTime);

        if (waitingTime.compareTo(minTransferTime) < 0) {
            return null;
        }

        Duration firstTripDuration = firstTrip.getRoute().getDurationBetweenStops(boardingIndex, firstTransferIndex);
        Duration secondTripDuration = secondTrip.getRoute().getDurationBetweenStops(secondTransferIndex, destinationIndex);
        Duration totalDuration = firstTripDuration.plus(waitingTime).plus(secondTripDuration);

        return new TransferJourney(firstTrip, secondTrip, transferStop, boardingIndex, firstTransferIndex, secondTransferIndex, destinationIndex, waitingTime, totalDuration, firstDepartureDateTime, secondArrivalDateTime);
    }

    private LocalDate findFirstTripDate(Trip trip, int boardingIndex, LocalDate travelDate) {
        LocalDateTime boardingDateTime = LocalDateTime.of(travelDate, trip.getArrivalTimeAt(boardingIndex));
        Duration durationToStop = trip.getRoute().getDurationToStop(boardingIndex);

        return boardingDateTime.minus(durationToStop).toLocalDate();
    }

    private LocalDate findSecondTripDate(Trip trip, int transferIndex, LocalDateTime earliestBoarding) {
        Duration durationToStop = trip.getRoute().getDurationToStop(transferIndex);
        LocalDateTime earliestDeparture = earliestBoarding.minus(durationToStop);
        LocalDate candidateDate = earliestDeparture.toLocalDate();

        if (trip.getDepartureTime().isBefore(earliestDeparture.toLocalTime())) {
            candidateDate = candidateDate.plusDays(1);
        }

        while (trip.getDayType() != getDayType(candidateDate)) {
            candidateDate = candidateDate.plusDays(1);
        }

        return candidateDate;
    }
}
