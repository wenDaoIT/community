package top.tom666.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.tom666.community.annotation.LoginRequired;
import top.tom666.community.entity.User;
import top.tom666.community.service.FollowServie;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.HostHolder;

import javax.annotation.Resource;

/**
 * @author: liujisen
 * @date： 2022-09-10
 */
@Controller
public class FollowController {
    @Resource
    private FollowServie followServie;
    @Resource
    private HostHolder hostHolder;

    @PostMapping("/follow")
    @ResponseBody
    @LoginRequired
    public String follow(int entityType, int entityId){
        User user = hostHolder.getUser();

        followServie.follow(user.getId(),entityType,entityId);

        return CommunityUtils.getJSONString(0,"已关注");
    }

    @PostMapping("/unfollow")
    @ResponseBody
    @LoginRequired
    public String unfollow(int entityType, int entityId){
        User user = hostHolder.getUser();

        followServie.unfollow(user.getId(),entityType,entityId);

        return CommunityUtils.getJSONString(0,"已取消关注");
    }

}
