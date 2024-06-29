package com.faker.project.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 动态构造 Logger 对象
 */
@SuppressWarnings("all")
@Slf4j(topic = "topicName")
public class LogbackHolder {

    /** 根据名称获取 logger 实例 */
    public static Logger getLogger(String name) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        // 如果没有创建 logger
        if (loggerContext.exists(name) == null) {
            // 自己动态构造 logger 对象
            return buildLogger(name);
        }
        return loggerContext.getLogger(name);
    }

    /** 动态构造logger实例, 实现输出打印文件 */
    private static Logger buildLogger(String name) {

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(name);

        // 配置 rollingFileAppender
        RollingFileAppender rollingFileAppender = new RollingFileAppender();
        rollingFileAppender.setName(name);
        rollingFileAppender.setContext(loggerContext);

        // 配置 rollingPolicy 滚动压缩策略
        TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
        rollingPolicy.setFileNamePattern("/tmp/log/" + name + ".%d{yyyyMM}.log"); // 设置文件压缩滚动路径: %d 指的时第几个文件
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.start();

        // 配置 encoder 编码规则
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setCharset(StandardCharsets.UTF_8);
        encoder.setPattern("%msg%n");
        encoder.setContext(loggerContext);
        encoder.start();

        rollingFileAppender.setRollingPolicy(rollingPolicy);
        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();

        // 配置 logger: appender绑定到logger上
        logger.addAppender(rollingFileAppender);
        logger.setAdditive(false);
        logger.setLevel(Level.INFO);

        return logger;
    }

    public static void main(String[] args) {
        getLogger("faker").info("dynamic construct logback..."); // 会生成日志文件
        log.info("use slf4j log");
    }
}
