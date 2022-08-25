package top.tom666.community.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.tom666.community.CommunityApplication;
import top.tom666.community.util.MailClient;

/**
 * @author: liujisen
 * @date： 2022-08-25
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Test
    public void testSentMail(){
//        mailClient.sendMail("691373807@qq.com","项目测试邮件","hello");
        mailClient.sendMail("468440123@qq.com","项目测试邮件","hello");
    }
}
