package com.imooc.ecommerce.controller;

import com.imooc.ecommerce.service.NacosClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/nacos-client")
public class NacosClientController {
    final NacosClientService nacosClientService;

    public NacosClientController(NacosClientService nacosClientService) {
        this.nacosClientService = nacosClientService;
    }

    @GetMapping("/service_instance")
    public List<ServiceInstance> getNacosClientInstancesByServiceId(@RequestParam(name = "service_id")String serviceId) {
        return nacosClientService.getNacosClientInfo(serviceId);
    }
}
