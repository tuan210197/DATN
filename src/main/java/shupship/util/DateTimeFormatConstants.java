package shupship.util;

import java.time.format.DateTimeFormatter;

public abstract class DateTimeFormatConstants {
    private DateTimeFormatConstants() {
        // Private constructor
    }

    public static final String LOCAL_DATE_FORMAT = "dd-MM-yyyy";
    public static final String LOCAL_DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);
    public static final DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT);
}
