package top.tom666.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * @author: liujisen
 * @date： 2022-08-21
 */
@Configuration
public class AlphaConfig {

    @Bean
    public SimpleDateFormat simpleFormatter(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
