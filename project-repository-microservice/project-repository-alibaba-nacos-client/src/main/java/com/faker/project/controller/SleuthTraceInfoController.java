package com.faker.project.controller;

import com.faker.project.service.SleuthTraceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 打印跟踪信息
 * */
@Slf4j
@RestController
@RequestMapping("/sleuth")
public class SleuthTraceInfoController {

    private final SleuthTraceInfoService traceInfoService;

    public SleuthTraceInfoController(SleuthTraceInfoService traceInfoService) {
        this.traceInfoService = traceInfoService;
    }

    /**
     * 打印日志跟踪信息
     * */
    @GetMapping("/trace-info")
    public void logCurrentTraceInfo() {
        traceInfoService.logCurrentTraceInfo();
    }
}
