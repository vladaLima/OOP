package model;

public enum TransportType {
    BUS(25, 80),
    TROLLEYBUS(20, 90),
    TRAM(18, 120),
    ELECTROBUS(25, 75),
    TAXI(40, 4),
    METRO(45, 1000),
    FERRY(20, 300);

    private final int avgSpeedKmh;
    private final int capacity;

    TransportType(int avgSpeedKmh, int capacity) {
        this.avgSpeedKmh = avgSpeedKmh;
        this.capacity = capacity;
    }

    public int getAvgSpeedKmh() {
        return avgSpeedKmh;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getTransportType() {
        return switch (this) {
            case BUS -> "автобус";
            case TROLLEYBUS -> "троллейбус";
            case TRAM -> "трамвай";
            case ELECTROBUS -> "электробус";
            case TAXI -> "такси";
            case METRO -> "метро";
            case FERRY -> "паром";
        };
    }

}
