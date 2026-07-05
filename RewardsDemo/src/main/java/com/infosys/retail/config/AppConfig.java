package com.infosys.retail.config;

import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
    @Bean
    public ConcurrentMapCacheManager getConcurrentMapCacheManager() {
    	return new ConcurrentMapCacheManager();
    }

}
