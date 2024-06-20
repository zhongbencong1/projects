package com.faker.project.schedule;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

@Slf4j
public class TimeHandle {
    public static void main1(String[] args) {
        String date1 = "2023-11-12 14:04:00";
        String date2 = "2023-11-13 14:04:00";
        long week = DateUtil.between(DateUtil.parseDate(date1), DateUtil.parseDate(date2), DateUnit.WEEK);
        long day = DateUtil.between(DateUtil.parseDate(date1), DateUtil.parseDate(date2), DateUnit.DAY);
        long hour = DateUtil.between(DateUtil.parseDate(date1), DateUtil.parseDate(date2), DateUnit.HOUR);
        long minute = DateUtil.between(DateUtil.parseDate(date1), DateUtil.parseDate(date2), DateUnit.MINUTE);
        log.info("两个时间相差: {}周, {}天, {}小时, {}分钟", week, day, hour, minute);
    }

    /**
     * stopWatch计时器: 测量代码执行时间的工具类
     */
    @SneakyThrows
    public static void main2(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("task1");
        Thread.sleep(1000);
        stopWatch.stop();
        log.info("task1 耗时毫秒: {}", stopWatch.getTotalTimeMillis());
    }
}
