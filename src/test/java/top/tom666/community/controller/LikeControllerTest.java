package top.tom666.community.controller;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.tom666.community.CommunityApplication;
import top.tom666.community.entity.User;
import top.tom666.community.service.LikeService;
import top.tom666.community.util.HostHolder;

import javax.annotation.Resource;

/**
 * @author: liujisen
 * @date： 2022-09-10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
class LikeControllerTest {
    @Autowired
    private LikeService likeService;

    @Resource
    private LikeController likeController;
    @Resource
    private HostHolder hostHolder;

    @Before
    public void setUser(){
        HostHolder hostHolder = new HostHolder();
        User user = new User();
        user.setId(150);
        user.setUsername("testzxh");
        hostHolder.setUser(user);
    }

    @Test
    void like() {
        likeService.like(150,0,90);
        long likeCount = likeService.selectLikeCount(0, 90);
        System.out.println(likeCount);
        likeController.like(0,90);
    }
}