package com.faker.project.schedule;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.faker.project.Entity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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


    /**
     * 一些关于map的处理思路
     */
    private static HashMap<String, Set<Entity>> listMap = new HashMap<String, Set<Entity>>() {
        @Override
        public Set<Entity> get(Object key) {
            if (Objects.isNull(super.get(key))) {
                listMap.put(String.valueOf(key), new HashSet<>());
            }
            return super.get(key);
        }
    };

    public static void main(String[] args) {
        HashSet<Entity> entities = new HashSet<>();
        entities.add(new Entity("no1", "faker1", new Entity.DataInfo()));
        listMap.put("1", entities);
        listMap.get("1").add(new Entity("no2", "faker2", new Entity.DataInfo()));
        listMap.get("2").add(new Entity("no2", "faker2", new Entity.DataInfo()));
        log.info(listMap.toString());
        test(listMap);
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("name", "faker1");
        log.info(objectObjectHashMap.get("123"));
    }

    private static Map<String, Set<Entity>> mapSet= new HashMap<>();

    public static void main4(String[] args) {
        HashSet<Entity> entities = new HashSet<>();
        entities.add(new Entity("no1", "faker1", new Entity.DataInfo()));
        entities.add(new Entity("no2", "faker2", new Entity.DataInfo()));
        mapSet.put("12", entities);
        HashSet<Entity> entities2 = new HashSet<>();
        entities2.add(new Entity("no3", "faker3", new Entity.DataInfo()));
        entities2.add(new Entity("no4", "faker4", new Entity.DataInfo()));
        mapSet.get("12").addAll(entities2);
        log.info(mapSet.toString());
        HashSet<Entity> entities3 = new HashSet<>(entities2);
        log.info(entities3.toString());
    }

    public static void test(HashMap<String, Set<Entity>> map) {
        log.info(map.get("3").toString()); // 返回空数组, 传递的对象会调用该对象被重写过的get方法
    }

}
