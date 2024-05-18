package com.faker.project.stream.custom;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义输出信道: 参考 org.springframework.cloud.stream.messaging.Source接口
 */
public interface CustomSource {
    // 字符串必须和 "output" 区分开, 该字符串代表唯一通信信道
    String OUTPUT = "customOutput";

    /** 输出信道的名称是 customOutput, 需要使用 Stream 绑定器在 yml 文件中声明 */
    @Output(CustomSource.OUTPUT)
    MessageChannel customOutput();
}
