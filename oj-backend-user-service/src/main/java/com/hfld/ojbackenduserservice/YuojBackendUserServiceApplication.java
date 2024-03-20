package com.hfld.ojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类（项目启动入口）
 *
 * @author hfldqwe
 *  
 */
// todo 如需开启 Redis，须移除 exclude 中的内容
@SpringBootApplication
@MapperScan("com.hfld.ojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.hfld")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.hfld.ojbackendserviceclient.service"})
public class YuojBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuojBackendUserServiceApplication.class, args);
    }

}
