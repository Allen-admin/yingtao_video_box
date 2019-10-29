package com.k365.video_common.util;

import com.k365.video_common.constant.DateStyleEnum;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author Gavin
 * @date 2019/6/20 15:46
 * @description：
 */
public class DateUtil {

    /**
     * Date -> String
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateFormat(Date date, DateStyleEnum pattern) {
        if (date == null) date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(pattern.value().toString());
        return sf.format(date);
    }


    /**
     * String -> Date
     *
     * @param date
     * @param pattern
     * @return
     * @throws Exception
     */
    public static Date formatDate(String date, DateStyleEnum pattern) throws Exception {
        if (date == null) throw new Exception("dataFormat date str is null ");
        SimpleDateFormat sf = new SimpleDateFormat(pattern.value().toString());
        return sf.parse(date);
    }

    /**
     * 获取 今天剩余时间（秒值）
     */
    public static long getSurplusSecondOfToday(){
        long nowSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        return  LocalDateTime.of(LocalDate.now(), LocalTime.MAX).toEpochSecond(ZoneOffset.of("+8")) - nowSecond;
    }

    /**
     * 获取 今年一个随机时间（過去的時間）
     */
    public static Date getRandomDateOfYear(){
        LocalDateTime now = LocalDateTime.now();
        return  new Date(LocalDateTime.of(LocalDate.of(now.getYear(),
                (int)(Math.random() * now.getMonth().getValue() + 1),
                (int)(now.getDayOfMonth() * Math.random()) + 1),
                LocalTime.of((int)(now.getHour() * Math.random() + 1),
                        (int)(now.getMinute() * Math.random() + 1),
                        now.getSecond())
        ).toInstant(ZoneOffset.of("+8")).toEpochMilli()) ;
    }
}
