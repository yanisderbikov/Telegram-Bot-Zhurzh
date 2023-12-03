package com.zhurzh.commonnodeservice.configuration;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.zhurzh.utils.CryptoTool;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableScheduling
@Configuration
@Log4j
@PropertySource("classpath:common-node-service.properties")
@EnableJpaRepositories(basePackages = "com.zhurzh.commonjpa.dao")
@EntityScan(basePackages = "com.zhurzh.commonjpa.entity")
@ComponentScan
public class NodeConfiguration {// это как-то связывает со спрингом чтобы все заработало

    @Value("${version.node}")
    private String version;
    @Value("${salt}")
    private String salt;

    @Bean
    public CryptoTool getCryptoTool() {
        log.debug("NODE VERSION: " + version);
        return new CryptoTool(salt);
    }

}
