package com.bindada.syscourse.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

@Slf4j
public class DayMapUtil {

    private static HashMap<String,Integer> hashMap=new HashMap<String,Integer>();

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
    private static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

    public static  Integer getDay(String key){
        hashMap.put("星期一",0);
        hashMap.put("星期二",1);
        hashMap.put("星期三",2);
        hashMap.put("星期四",3);
        hashMap.put("星期五",4);
        hashMap.put("星期六",5);
        hashMap.put("星期日",6);
        return hashMap.get(key);
    }

    public static Date getStartDay() {
        Date date = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,-getDay(simpleDateFormat.format(date)));
        Date newTime = calendar.getTime();
        String startDay = simpleDateFormat1.format(newTime);
        try {
            return simpleDateFormat1.parse(startDay);
        } catch (ParseException e) {
            e.printStackTrace();
            log.info("空指针异常");
            return newTime;
        }
    }

    public static Date getEndDay(){
        Date startDay = getStartDay();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(startDay);
        calendar.add(calendar.DATE,7);
        return calendar.getTime();
    }
}
