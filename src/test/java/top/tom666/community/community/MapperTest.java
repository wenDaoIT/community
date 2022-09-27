package top.tom666.community.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.tom666.community.CommunityApplication;
import top.tom666.community.dao.DiscussPostMapper;
import top.tom666.community.dao.LoginTicketMapper;
import top.tom666.community.dao.MessageMapper;
import top.tom666.community.dao.UserMapper;
import top.tom666.community.entity.DiscussPost;
import top.tom666.community.entity.LoginTicket;
import top.tom666.community.entity.Message;
import top.tom666.community.entity.User;

import java.util.Date;
import java.util.List;

/**
 * @author: liujisen
 * @date： 2022-08-23
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private MessageMapper messageMapper;
    private static final Logger logger = LoggerFactory.getLogger(MapperTest.class);

    @Test
    public void selectByidTest(){
        User user=userMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    public void selectDiscussPost(){
        List<DiscussPost> discussPostList=discussPostMapper.selectDiscussPosts(0,0,10,0);
        for (DiscussPost post:discussPostList){
            System.out.println(post);
        }
        int row = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(row);
    }
    @Test
    public void testLogger(){
        System.out.println(logger.getName());
        logger.debug("debuglog");
        logger.info("infolog");
        logger.error("errorlog");
    }

    @Test
    public void testInsertToken(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(10);
        loginTicket.setStatus(1);
        loginTicket.setTicket("2112131");
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 *10));
        System.out.println(loginTicketMapper.insertLoginTicket(loginTicket));
    }

    @Test
    public void testMessageMapper(){
//        List<Message> messageList = messageMapper.selectConversions(111,0,20);
//        for (Message message : messageList){
//            System.out.println(message);
//        }
        System.out.println(messageMapper.selectLetter("111_112",0,10));

    }
}
