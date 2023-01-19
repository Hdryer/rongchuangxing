package com.bindada.syscourse.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 *  生成订单号的工具类
 * @Author bindada
 * @Date  2022-07-28
 *
**/
public class UUidUtil {

    /**
     * 评价表的id生成
     * */
    public static String generateUUID(){
        //标识日期
        SimpleDateFormat sdf = new SimpleDateFormat("hhmmss");
        String dayTime = sdf.format(new Date());

        //生成6位的后缀码
        String pre="";
        for (int i=1;i<=6;i++){
            pre=pre + String.valueOf(new Random().nextInt(9));
        }

        //生成位UUID哈希码
        int hashCode = UUID.randomUUID().toString().hashCode();
        if(hashCode< 0) {
            hashCode = -hashCode;
        }
        String value = dayTime+String.format("%010d",hashCode)+pre;
        return value;
    }
}
