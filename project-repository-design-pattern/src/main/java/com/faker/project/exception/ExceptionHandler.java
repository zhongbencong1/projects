package com.faker.project.exception;

/**
 * 异常捕捉
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    /** 重新打信息 */
    @Override
    public void uncaughtException(Thread t, Throwable e) {

        StackTraceElement[] ses = e.getStackTrace();
        System.err.println("Exception in thread \"" + t.getName() + "\" " + e);

        for (StackTraceElement se : ses) {
            System.err.println("\tat " + se);
        }

        Throwable ec = e.getCause();
        if (null != ec) {
            uncaughtException(t, ec);
        }
    }
}
