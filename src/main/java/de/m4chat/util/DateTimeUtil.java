package de.m4chat.util;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter
      .ofPattern("HH:mm dd.MM")
      .withZone(ZoneId.systemDefault());

  public static String formatDate(Timestamp dateTime) {
    if (dateTime == null) {
      return "";
    }
    return FORMATTER.format(dateTime.toInstant()); // Convert Timestamp to Instant
  }
}
