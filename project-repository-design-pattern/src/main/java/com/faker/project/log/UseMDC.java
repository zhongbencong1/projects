package com.faker.project.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * MDC : 请求执行轨迹 日志链路追踪 日志grep mdc埋点
 * 微服务中 以traceId 承载 mdc的id, 可以进行微服务间的链路追踪, 放在请求头中进行传递
 */
@Slf4j
public class UseMDC {

    // 使用 MDC 之前, 需要先去配置logback.xml %X{REQUEST_ID}
    private static final String FLAG = "REQUEST_ID";

    // 第一个例子
    private static void mdc01() {
        MDC.put(FLAG, UUID.randomUUID().toString());
        log.info("log in mdc01");
        mdc02();

        log.info("MDC FLAG is: [{}]", MDC.get(FLAG));
        MDC.remove(FLAG);
        log.info("after remove MDC FLAG");
        // MDC.clear是清除所有的 key
    }

    private static void mdc02() {
        log.info("log in mdc02");
    }

    // 第二个例子, 多线程
    static class MyHandler extends Thread {

        private final String name;

        public MyHandler(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            MDC.put(FLAG, UUID.randomUUID().toString());
            log.info("start to process: [{}]", this.name);
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                log.info(e.getMessage());
            }
            log.info("done to process: [{}]", this.name);
            MDC.remove(FLAG);
        }
    }

    private void MultiThreadUseMdc() {
        new MyHandler("faker").start();
        new MyHandler("the shy").start();
    }


    public static void main(String[] args) {
        mdc01(); // mdc简单用法
        new UseMDC().MultiThreadUseMdc(); // mdc多线程用法, 每个线程都会使用各自的MDC的信息
    }
}
