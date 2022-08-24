package top.tom666.community.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.tom666.community.dao.DiscussPostMapper;
import top.tom666.community.dao.UserMapper;
import top.tom666.community.entity.DiscussPost;
import top.tom666.community.entity.User;

import java.util.List;

/**
 * @author: liujisen
 * @date： 2022-08-23
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Test
    public void selectByidTest(){
        User user=userMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    public void selectDiscussPost(){
        List<DiscussPost> discussPostList=discussPostMapper.selectDiscussPosts(0,0,10);
        for (DiscussPost post:discussPostList){
            System.out.println(post);
        }
        int row = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(row);
    }

}
