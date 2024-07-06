package com.faker.project.thread;

import java.util.Map;

/**
 * 获取运行时线程堆栈
 */
public class GetThreadStack {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        Map<Thread, StackTraceElement[]> ts = Thread.getAllStackTraces(); // 获取所有线程

        ts.keySet().forEach(thread -> {
            sb.append(thread.getName()).append(":").append(thread.getId()).append("\n");
            for (StackTraceElement ste : ts.get(thread)) {
                sb.append(ste).append("\n");
            }
            // 隔离开每一个线程
            sb.append("#######").append("\n");
        });

        System.out.println(sb);
    }
}
