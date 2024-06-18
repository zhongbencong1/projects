package com.faker.project.schedule;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

public class TimeHandle {
    public static void main(String[] args) {
        String date1 = "2023-11-12 14:04:00";
        String date2 = "2023-11-13 14:04:00";
        long day = DateUtil.between(DateUtil.parseDate(date1), DateUtil.parseDate(date2), DateUnit.WEEK);
        System.out.println("两个时间相差 " + day + " 天");
    }

}
