package com.lsyf.pay.logger.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author liufei
 * @date 2018-04-18
 */
public class TimeUtil {
    public static final DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
    public static final DateTimeFormatter yyyyMM_CN = DateTimeFormatter.ofPattern("yyyy年MM月");
    public static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter yyyyMMdd_CN = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    public static final DateTimeFormatter yyyyMMdd2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    public static final DateTimeFormatter yyyyMMdd3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter ddMMyyyy = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter yyyyMMddHHmmss_common = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static LocalDateTime toLocalDateTime(Date date) {
        return toLocalDateTime(date, ZoneOffset.UTC);
    }

    public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
        return date == null ?
                null :
                date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public static Date toDate(LocalDate ld) {
        return toDate(ld, ZoneOffset.UTC);
    }

    public static Date toDate(LocalDate ld, ZoneId zoneId) {
        return ld == null ?
                null :
                Date.from(ld.atStartOfDay(zoneId).toInstant());
    }

    public static Date toDate(LocalDateTime ldt) {
        return toDate(ldt, ZoneOffset.UTC);
    }


    public static Date toDate(LocalDateTime ldt, ZoneId zoneId) {
        return ldt == null ?
                null :
                Date.from(ldt.atZone(zoneId).toInstant());
    }


    public static Date toDate(ZonedDateTime zdt) {
        return zdt == null ?
                null :
                Date.from(zdt.toInstant());
    }

    public static String str(Date date, DateTimeFormatter dtf) {
        return date == null ?
                null : toLocalDateTime(date).format(dtf);
    }

    public static String str(Date date) {
        return str(date, ZoneOffset.UTC);
    }


    /**
     * 指定解析时间时区
     *
     * @param date
     * @param dtf
     * @param zoneId
     * @return
     */
    public static Date parseTime(String date,
                                 DateTimeFormatter dtf,
                                 ZoneId zoneId) {
        return toDate(ZonedDateTime.parse(date, dtf.withZone(zoneId)));
    }

    /**
     * 指定解析日期时区
     *
     * @param date
     * @param dtf
     * @param zoneId
     * @return
     */
    public static Date parseDate(String date,
                                 DateTimeFormatter dtf,
                                 ZoneId zoneId) {
        return toDate(ZonedDateTime.of(LocalDate.parse(date, dtf), LocalTime.ofSecondOfDay(0), zoneId));
    }


    public static String str(Date date, ZoneId zoneId) {
        return date == null ?
                null : toLocalDateTime(date, zoneId).format(yyyyMMddHHmmss_common);
    }

    public static String now(DateTimeFormatter dtf) {
        return LocalDateTime.now(ZoneOffset.UTC).format(dtf);
    }

    public static Date nowDate() {
        return Date.from(Instant.now());
    }

    public static LocalDate nowLocalDate() {
        return LocalDate.now(ZoneOffset.UTC);
    }

    public static LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }


}
