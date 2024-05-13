package com.faker.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通过 kafka 传递的消息对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessage {

    private Integer id;

    private String projectName;
}
