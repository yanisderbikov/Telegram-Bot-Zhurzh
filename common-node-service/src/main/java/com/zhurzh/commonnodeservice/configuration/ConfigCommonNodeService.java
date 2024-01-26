package com.zhurzh.commonnodeservice.configuration;

import com.zhurzh.commonutils.utils.CryptoTool;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableScheduling
@Configuration
@Log4j
@PropertySource("classpath:common-node-service.properties")
@EnableJpaRepositories(basePackages = "com.zhurzh.commonjpa.dao")
@EntityScan(basePackages = "com.zhurzh.commonjpa.entity")
@ComponentScan(basePackages = "com.zhurzh.commonjpa")
public class ConfigCommonNodeService {// это как-то связывает со спрингом чтобы все заработало
    @Value("${salt}")
    private String salt;

    @Bean
    public CryptoTool getCryptoTool() {
        return new CryptoTool(salt);
    }

}
