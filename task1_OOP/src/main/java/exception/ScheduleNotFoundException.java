package exception;

import java.time.LocalDate;

/**
 * Сообщает об отсутствии расписания для остановки и заданного периода.
 */
public class ScheduleNotFoundException extends TransportException {
  /**
   * Создаёт исключение для типа дня, по которому нет расписания.
   *
   * @param dayType обозначение типа дня
   */
  public ScheduleNotFoundException(String dayType) {
    super("No schedule found for " + dayType);
  }
  /**
   * Создаёт исключение для даты, по которой нет расписания.
   *
   * @param date дата, для которой не найдено расписание
   */
  public ScheduleNotFoundException(LocalDate date) {
    super("No schedule found for " + date);
  }
}
