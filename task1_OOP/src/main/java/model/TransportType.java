package model;

/**
 * Виды городского транспорта с характеристиками скорости и вместимости.
 */
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

    /**
     * Возвращает среднюю скорость данного вида транспорта.
     *
     * @return средняя скорость в километрах в час
     */
    public int getAvgSpeedKmh() {
        return avgSpeedKmh;
    }

    /**
     * Возвращает вместимость транспорта.
     *
     * @return количество пассажиров
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Возвращает название вида транспорта на русском.
     *
     * @return название транспорта
     */
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
