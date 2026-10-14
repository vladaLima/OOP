package exception;

import java.time.LocalDate;

public class ScheduleNotFoundException extends TransportException {
  public ScheduleNotFoundException(String dayType) {
    super("No schedule found for " + dayType);
  }
  public ScheduleNotFoundException(LocalDate date) {
    super("No schedule found for " + date);
  }
}
