package com.faker.project.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 打印完整的异常栈信息
 * */
@Slf4j
public class CompleteException {
    public static void main(String[] args) {
        // 打印完整的异常栈信息
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        log.info("new CompleteException().func5: {}", new CompleteException().func5("123"));
        new CompleteException().func3();
    }

    private void func1() throws Exception {
        throw new Exception("func1 has exception...");
    }

    private void func2() throws Exception {
        try {
            func1();
        } catch (Exception ex) {
            throw new Exception("func2 has exception...", ex);
        }
    }

    private void func3() {
        try {
            func2();
        } catch (Exception ex) {
            throw new RuntimeException("func3 has exception...", ex);
        }
    }

    public String func4(String str) {
        int integer = Integer.parseInt(str);
        return String.valueOf(integer);
    }

    /** finally的return会覆盖前面的return */
    public String func5(String str) {
        try {
            return func4(str);
        } catch (ClassCastException ex) {
            log.error("func5 has exception...", ex);
            return "ClassCastException";
        } finally {
            log.error("func5 finally");
            return "finally";
        }
    }
}
