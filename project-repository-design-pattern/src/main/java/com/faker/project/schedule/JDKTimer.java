package com.faker.project.schedule;

import java.util.Timer;
import java.util.TimerTask;

/**
 * jdk 自带定时任务 定时器
 * 支持注册多个定时任务
 */
public class JDKTimer {
    public static void main(String[] args) {
        //1-创建Timer对象
        Timer timer = new Timer();

        //2-设定定时效果：延迟5秒开始执行, 每2秒执行一次
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("do something");
            }
        }, 5000, 1000 * 2);

        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                System.out.println("do something");
            }
        },4000, 1000 * 2);


        System.out.println("BBB");
    }

}
