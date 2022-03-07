package com.zx.fss.utils;

import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @author 张新
 * @title: DateUil
 * @projectName rpts
 * @description: 时间转换工具
 * @date 2021/6/416:41
 */
public class DateUtil {

    public static String PATTERN_YYYYMM = "yyyyMM";
    public static String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String PATTERN_YYYYMMDDHH = "yyyyMMddHH";
    public static String PATTERN_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static String PATTERN_YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public static String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static String PATTERN_YYYYMMDD = "yyyyMMdd";
    public static String PATTERN_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public static String PATTERN_HTTP = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static String PATTERN_YYYYMMDD_CHINESE = "yyyy年MM月dd日";
    public static String PATTERN_YYYYMMDD_HHMMSS_CHINESE = "yyyy年MM月dd日 HH:mm:ss";
    public static String PATTERN_YYYYMMDD_SLASH = "yyyy/MM/dd";
    public static String PATTERN_YYYYMMDD_HHMMSS_SLASH = "yyyy/MM/dd  HH:mm:ss";
    public static String PATTERN_YYYY_MM_DD_00 = "yyyy-MM-dd 00:00:00";
    public static String PATTERN_HHMMSS = "HH:mm:ss";
    public static String UTC_PATTERN_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static String PATTERN_YYYY_MM = "yyyy-MM";

    /**
     * 将Long类型的时间戳转换成String 类型的时间格式，时间格式为：yyyy-MM-dd HH:mm:ss
     */
    public static String timeToString(Long time, String format) {
        Assert.notNull(time, "time is null");
        // "yyyy-MM-dd HH:mm:ss"
        DateTimeFormatter formatString = DateTimeFormatter.ofPattern(format);
        return formatString.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }

    /**
     * 将字符串转日期成Long类型的时间戳，格式为：yyyy-MM-dd HH:mm:ss
     */
    public static Long timeToLong(String time) {
        Assert.notNull(time, "time is null");
        DateTimeFormatter formatString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(time, formatString);
        return LocalDateTime.from(parse).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    /**
     * localDate 格式化
     */
//    public static String localDateFormat(LocalDate localDate, String format){
//        Assert.notNull(localDate, "time is null");
//        // "yyyy-MM-dd HH:mm:ss"
//        String localDateStr = DateTranslate.timeToString(localDate2Second(localDate), format);
//        return localDateStr;
//    }
    /**
     * localDate 格式化
     */
//    public static String localDateTimeFormat(LocalDateTime localDateTime, String format){
//        Assert.notNull(localDateTime, "time is null");
//        // "yyyy-MM-dd HH:mm:ss"
//        String localDateTimeStr = DateTranslate.timeToString(localDateTime2Second(localDateTime), format);
//        return localDateTimeStr;
//    }

    /**
     * 取本月第一天
     */
    public static LocalDate firstDayOfThisMonth() {
        LocalDate today = LocalDate.now();
        return today.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 取本月第一天(字符串)
     */
    public static String firstDayOfThisMonthStr() {
        String format = "yyyy-MM-dd";
        LocalDate localDate = firstDayOfThisMonth();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);
        String dateStr = localDate.format(fmt);
        return dateStr;
    }

    /**
     * 取本 下月第一天
     */
    public static LocalDate firstDayOfNextMonth() {
        LocalDate today = LocalDate.now();
        today = today.minusMonths(-1);
        return today.with(TemporalAdjusters.firstDayOfMonth());
    }
    public static String firstDayOfNextMonthStr() {
        String format = "yyyy-MM-dd";
        LocalDate localDate = firstDayOfNextMonth();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);
        String dateStr = localDate.format(fmt);
        return dateStr;
    }


    /**
     * 取本月第N天
     */
    public static LocalDate dayOfThisMonth(int n) {
        LocalDate today = LocalDate.now();
        return today.withDayOfMonth(n);
    }

    /**
     * 取本月最后一天
     */
    public static LocalDate lastDayOfThisMonth() {
        LocalDate today = LocalDate.now();
        return today.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 取本月最后一天
     */
    public static String lastDayOfThisMonthStr() {
        String format = "yyyy-MM-dd";
        LocalDate localDate = lastDayOfThisMonth();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);
        String dateStr = localDate.format(fmt);
        return dateStr;
    }

    /**
     * 获取本周一
     */
    public static LocalDateTime firstDayOfWeek(Long date) {
        // long转LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault());
        return localDateTime.with(DayOfWeek.MONDAY);
    }

    /**
     * 获取上周一
     */
    public static LocalDateTime firstDayOfLastWeek(Long date) {
        // long转LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault());
        LocalDateTime localDateTime1 = localDateTime.plusDays(-7);
        return localDateTime1.with(DayOfWeek.MONDAY);
    }

    /**
     * 获取上周一
     */
    public static LocalDateTime lastDay(Long date) {
        // long转LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault());
        LocalDateTime localDateTime1 = localDateTime.plusDays(-1);
        return localDateTime1;
    }

    /**
     * 取本月第一天的开始时间
     */
    public static LocalDateTime startOfThisMonth() {
        return LocalDateTime.of(firstDayOfThisMonth(), LocalTime.MIN);
    }

    /**
     * 取本月最后一天的结束时间
     */
    public static LocalDateTime endOfThisMonth() {
        return LocalDateTime.of(lastDayOfThisMonth(), LocalTime.MAX);
    }

    /**
     * LocalDate转时间戳
     */
    public static Long localDate2Second(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN).toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
    }

    /**
     * LocalDate转时间戳(秒)
     */
    public static Long localDateTime2Second(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * LocalDate转时间戳(毫秒)
     */
    public static Long localDateTime2Milli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * Date时间默认加一天
     */
    public static Date addDate(Date date) {
        return addDate(date, 1);
    }

    /**
     * Date时间加减指定天数
     */
    public static Date addDate(Date date, int n) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n); //把日期往后增加一天,整数  往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        return date;
    }


    /**
     * 锁对象
     */
    private static final Object lockObj = new Object();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new java.util.concurrent.ConcurrentHashMap<String, ThreadLocal<SimpleDateFormat>>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {

                        @Override
                        protected SimpleDateFormat initialValue() {
                            //System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 是用ThreadLocal SimpleDateFormat来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     * 时间格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    /**
     * 时间反格式化
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parse(String dateStr, String pattern) {
        Date date = null;
        try {
            date = getSdf(pattern).parse(dateStr);
        } catch (Exception e) {

        }
        return date;
    }

    /**
     * 本地时间转 UTC 时间字符串
     *
     * @param date
     * @return
     */
    public static String localToUtcString(Date date, String pattern) {
        SimpleDateFormat sdf = getSdf(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    /**
     * UTC 时间反格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date utcStringToUtcDate(String date, String pattern) {
        SimpleDateFormat sdf = getSdf(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try {
            utcDate = sdf.parse(date);
        } catch (Exception e) {

        }
        return utcDate;
    }

    /**
     * UTC 时间格式化
     *
     * @param date
     * @return
     */
    public static String utcDateToUtcString(Date date) {
        SimpleDateFormat sdf = getSdf("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }


    /**
     * UTC 时间字符串转本地时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date utcStringToLocalDate(String date, String pattern) {
        SimpleDateFormat sdf = getSdf(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date localDate = null;
        try {
            localDate = sdf.parse(date);
        } catch (Exception e) {

        }
        return localDate;
    }

    /**
     * 获取本周一的日期
     *
     * @return
     */
    public static String getMonday() {
        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();
        return DateUtil.format(mondayDate, PATTERN_YYYY_MM_DD);

    }

    /**
     * 获取本周日的日期
     *
     * @return
     */
    public static String getSunday() {
        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        cal.add(Calendar.DATE, 4 + cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        return format(sundayDate, PATTERN_YYYY_MM_DD);
    }

    /**
     * 获取某月的最后一天
     *
     * @param year  年份
     * @param month 月份
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        String day = format(cal.getTime(), PATTERN_YYYY_MM_DD);
        return day;
    }

    /**
     * 获取指定区间内随机时间
     *
     * @param beginDate
     * @param endDate
     * @param pattern
     * @return
     */
    public static Date randomDate(String beginDate, String endDate, String pattern) {
        try {
            Date start = parse(beginDate, pattern);
            Date end = parse(endDate, pattern);
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }

    //获取今天日期 年月日
    public static Date getTodayYmd() {
        Date now = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(now);
        // 将时分秒,毫秒域清零
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        return cal1.getTime();
    }

    //获取昨天日期 年月日
    public static Date getYesterdayYmd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public static String formatMyDate(Date targetDate) {
        return format(targetDate, PATTERN_YYYY_MM_DD_HHMMSS);
    }

    public static String formatMyDateNoLine(Date targetDate) {
        return format(targetDate, PATTERN_YYYYMMDDHHMMSS);
    }

    public static String formatMyDateNoLineLonger(Date targetDate) {
        return format(targetDate, PATTERN_YYYYMMDDHHMMSSSSS);
    }

    public static String formatMyDateShort(Date targetDate) {
        return format(targetDate, PATTERN_YYYY_MM_DD);
    }

    public static String formatPeriodDate(Date targetDate) {
        return format(targetDate, PATTERN_YYYY_MM);
    }

    /**
     * LocalDate转Date
     *
     * @param localDate
     * @return
     */
    public static Date localDateToDate(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 获取现在的日期
     *
     * @return
     */
    public static Date getNowDate() {
        LocalDate localDate = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    public static Date parseMyDateStr(String targetDate) {
        return parse(targetDate, PATTERN_YYYY_MM_DD_HHMMSS);
    }

    public static Date parseMyDateStrShort(String targetDate) {
        return parse(targetDate, PATTERN_YYYY_MM_DD);
    }

//    public static void main(String[] args) {
//        String s = firstDayOfThisMonthStr();
//        String s1 = lastDayOfThisMonthStr();
//        String s2 = firstDayOfNextMonthStr();
//        System.out.println(s+","+s1+","+s2);
//    }
}
