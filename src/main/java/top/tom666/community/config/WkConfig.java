package top.tom666.community.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author: liujisen
 * @date： 2022-09-27
 */
@Configuration
@Slf4j
public class WkConfig {
    @Value("${wk.image.storage}")
    private String wkImageStorage;
    @PostConstruct
    public void init(){
        //创建wk图片目录
        File file = new File(wkImageStorage);
        if (!file.exists()){
            file.mkdir();
            log.info("创建wk图片目录:"+ wkImageStorage);
        }
    }

}
