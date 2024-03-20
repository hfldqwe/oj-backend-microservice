package com.hfld.ojbackendjudgeservice;

import com.hfld.ojbackendjudgeservice.rabbitmq.CodeMqInitMain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.hfld")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.hfld.ojbackendserviceclient.service"})
public class YuojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        // 初始化消息队列
        CodeMqInitMain.doInitCodeMq();
        SpringApplication.run(YuojBackendJudgeServiceApplication.class, args);
    }
}