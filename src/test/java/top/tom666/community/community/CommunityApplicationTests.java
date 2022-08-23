package top.tom666.community.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import top.tom666.community.config.AlphaConfig;

@SpringBootTest
@ContextConfiguration(classes = ApplicationContext.class)
class CommunityApplicationTests implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
    @Test
    public void testApplication(){
        System.out.println(applicationContext);
    }

    @Test
    public void testGetBean(){
        AlphaConfig simpleDateFormat = applicationContext.getBean(AlphaConfig.class);
        System.out.println(simpleDateFormat.simpleFormatter());
    }
}
