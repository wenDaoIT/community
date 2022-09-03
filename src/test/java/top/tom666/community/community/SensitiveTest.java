package top.tom666.community.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.tom666.community.CommunityApplication;
import top.tom666.community.util.SensitiveFilter;

import javax.xml.ws.soap.Addressing;

/**
 * @author: liujisen
 * @date： 2022-09-02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {
    @Autowired
    SensitiveFilter sensitiveFilter;
    @Test
    public void filterTest(){
//        String result = sensitiveFilter.filter("这里可以色色，可以吸毒，我是sb，哈哈哈！！fabc");
        String result = sensitiveFilter.filter("fabcf");
        System.out.println(result);
     }
}
