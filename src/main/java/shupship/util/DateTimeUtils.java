package shupship.util;

import org.apache.commons.collections4.CollectionUtils;
import shupship.domain.model.Schedule;
import shupship.util.exception.HieuDzException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateTimeUtils {

    public static Date StringToDate(String date) throws ParseException {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        return myFormat.parse(date);
    }

    public static LocalDateTime StringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    public static LocalDateTime StringToLocalDateTime2(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return LocalDateTime.parse(date, formatter);
    }

    public static LocalDate StringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);
    }

    public static String localDateTimeFormat(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return date.format(formatter);
    }


    public static LocalDateTime instantToLocalDateTime(Instant time) {
        return LocalDateTime.ofInstant(time, ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public static Instant localDateTimeToInstant(LocalDateTime time) {
        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(time);
        return time.toInstant(zoneOffSet);
    }

    public static boolean isValidDate(String timeStr) {
        try {
            if (timeStr == null) {
                return false;
            }
            LocalDate ld = LocalDate.parse(timeStr, DateTimeFormatConstants.localDateFormatter);
            return timeStr.equals(toLocalDateString(ld));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidLocalDateTime(String timeStr) {
        try {
            if (timeStr == null) {
                return false;
            }
            LocalDateTime ld = LocalDateTime.parse(timeStr, DateTimeFormatConstants.localDateTimeFormatter);
            return timeStr.equals(toLocalDateTimeString(ld));
        } catch (Exception e) {
            return false;
        }
    }


    public static String toLocalDateString(LocalDate ld) {
        if (ld == null) {
            return null;
        }
        return DateTimeFormatConstants.localDateFormatter.format(ld);
    }

    public static String toLocalDateTimeString(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        return DateTimeFormatConstants.localDateTimeFormatter.format(ldt);
    }

    public static LocalDate getEndOfCurrentMonth(LocalDate ld) {
        return ld.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate getFirstOfCurrentMonth(LocalDate ld) {
        return ld.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static boolean isOverlapTime(LocalDateTime startDate1, LocalDateTime endDate1, LocalDateTime startDate2, LocalDateTime endDate2) {
        if (startDate1 == null || startDate2 == null) {
            return false;
        } else if ((startDate1.isBefore(endDate2) || startDate1.equals(endDate2))
                && (startDate2.isBefore(endDate1) || startDate2.equals(endDate1))) {
            return true;
        }
        return false;
    }

    public static boolean isBetweenDateRange(LocalDateTime currentDate, LocalDateTime startDate, LocalDateTime endDate) {
        return !(currentDate.isBefore(startDate) || currentDate.isAfter(endDate));
    }

    public static boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        LocalDate localDate1 = date1.toLocalDate();
        LocalDate localDate2 = date2.toLocalDate();
        return localDate1.isEqual(localDate2);
    }

    public static boolean isValidMonth(String month) {
        DateFormat parser = new SimpleDateFormat("MM-yyyy");
        try {
            Date convertedDate = parser.parse(month);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<LocalDate> getAllDatesBetweenTwoDates(
            LocalDate startDate, LocalDate endDate) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());
    }

    public static void validateOverlapTime(LocalDateTime fromDate, LocalDateTime toDate, List<Schedule> schedules) {
        if (CollectionUtils.isNotEmpty(schedules)) {
            schedules.forEach(e -> {
                if (e != null) {
                    LocalDateTime latestFromDate = e.getFromDate();
                    LocalDateTime latestToDate = e.getToDate();
                    if (DateTimeUtils.isOverlapTime(fromDate, toDate, latestFromDate, latestToDate)) {
                        throw new HieuDzException("Lịch bị trùng với lịch trước đó");
                    }
                }
            });
        }
    }
}
