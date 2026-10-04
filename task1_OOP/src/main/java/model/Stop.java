package model;

public record Stop(int StopID, String StopName) {

    public Stop {
        if (StopID <= 0) {
            throw new IllegalArgumentException("StopID must be positive");
        } else if (StopName == null || StopName.isBlank()) {
            throw new IllegalArgumentException("StopName cannot be null or blank");
        }
    }
}
