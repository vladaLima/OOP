package exception;

public class ScheduleNotFoundException extends TransportException {
  public ScheduleNotFoundException(String dayType) {
    super("No schedule found for " + dayType);
  }
}
