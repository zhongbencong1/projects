package com.faker.project.controller;

import com.faker.project.stream.DefaultSendService;
import com.faker.project.stream.custom.CustomSendService;
import com.faker.project.vo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 构建消息驱动
 * */
@Slf4j
@RestController
@RequestMapping("/stream-message")
public class MessageController {

    @Autowired
    private DefaultSendService defaultSendService;

    @Autowired
    private CustomSendService customSendService;

    /** 通过默认信道发送message */
    @GetMapping("/default-send")
    public void defaultSend() {
        defaultSendService.sendMessage(Message.defaultMessage());
    }

    /** 通过自定义信道发送message */
    @GetMapping("/custom-send")
    public void customSend() {
        customSendService.sendMessage(Message.defaultMessage());
    }
}
