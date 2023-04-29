package me.frankv.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ApplicationConfig {

    @Bean(name = "clock")
    public Clock applicationTimeZoneClock() {
        return Clock.system(ZoneId.of("Asia/Taipei"));
    }
}
