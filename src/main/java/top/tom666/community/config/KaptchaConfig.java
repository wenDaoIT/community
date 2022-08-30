package top.tom666.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author: liujisen
 * @date： 2022-08-27
 */
@Configuration

public class KaptchaConfig {
    @Bean
    public Producer kaptchaProducer(){
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.height","40");
        properties.setProperty("kaptcha.image.width","100");
        properties.setProperty("kaptcha.textproducer.font.size","32");
//        properties.setProperty("kaptcha.textproducer.char.string","虾仁滑蛋123456789");
        properties.setProperty("kaptcha.textproducer.char.string","123456789");
        properties.setProperty("kaptcha.textproducer.char.length","4");
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
