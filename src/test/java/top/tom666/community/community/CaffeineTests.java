package top.tom666.community.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.tom666.community.CommunityApplication;
import top.tom666.community.entity.DiscussPost;
import top.tom666.community.service.DiscussPostService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: liujisen
 * @date： 2022-10-08
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class CaffeineTests {
    @Resource
    private DiscussPostService postService;

    @Test
    public void initDataForTest(){
        for (int i = 0; i < 30000; i++) {
            DiscussPost post = new DiscussPost();
            post.setUserId(111);
            post.setTitle("2023 互联网");
            post.setContent("今年的就业形势，确实不容乐观。过了个年，仿佛跳水一般，整个讨论区哀鸿遍野！19届真的没人要了吗？！18届被优化真的没有出路了吗？！大家的&ldquo;哀嚎&rdquo;与&ldquo;悲惨遭遇&rdquo;牵动了每日潜伏于讨论区的牛客小哥哥小姐姐们的心，于是牛客决定：是时候为大家做点什么了！为了帮助大家度过&ldquo;寒冬&rdquo;，牛客网特别联合60+家企业，开启互联网求职暖春计划，面向18届&amp;19届，拯救0 offer！");
            post.setCreateTime(new Date());
            post.setScore(Math.random() * 2000);
            postService.addDiscussPost(post);
        }
    }

    @Test
    public void testCache(){
        System.out.println(postService.getDiscussPosts(0,0,10,1));
        System.out.println(postService.getDiscussPosts(0,0,10,1));
        System.out.println(postService.getDiscussPosts(0,0,10,1));
        System.out.println(postService.getDiscussPosts(0,0,10,0));
    }

}
