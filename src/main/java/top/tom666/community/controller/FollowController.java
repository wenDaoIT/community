package top.tom666.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.tom666.community.annotation.LoginRequired;
import top.tom666.community.entity.Page;
import top.tom666.community.entity.User;
import top.tom666.community.service.FollowServie;
import top.tom666.community.service.UserService;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.Constant;
import top.tom666.community.util.HostHolder;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: liujisen
 * @date： 2022-09-10
 */
@Controller
public class FollowController implements Constant {
    @Resource
    private FollowServie followServie;
    @Resource
    private HostHolder hostHolder;
    @Resource
    private UserService userService;

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

    @GetMapping("/followee/{userId}")
    public String getFollowees(@PathVariable int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if (user == null){
            throw new RuntimeException("用户不存在");
        }
        model.addAttribute("user",user);
        page.setLimit(5);
        page.setPath("/followees/"+ userId);
        page.setCount((int) followServie.findFolloweeCount(userId,ENTITY_TYPE_USER));

        List<Map<String,Object>> userList = followServie.findFollowees(userId,ENTITY_TYPE_USER,page.getOffset(),page.getLimit());
        if (userList!=null){
            for (Map map:userList){
                User u = (User) map.get("user");
                boolean hasFollowed = followServie.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_USER,u.getId());
                map.put("hasFollowed",hasFollowed);
            }
        }
        model.addAttribute("users",userList);
        return "site/followee";
    }

    @GetMapping("/follower/{userId}")
    public String getFollowers(@PathVariable int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if (user == null){
            throw new RuntimeException("用户不存在");
        }
        model.addAttribute("user",user);
        page.setLimit(5);
        page.setPath("/followers/"+ userId);
        page.setCount((int) followServie.fingFollowerCount(ENTITY_TYPE_USER,userId));

        List<Map<String,Object>> userList = followServie.findFollowers(userId,ENTITY_TYPE_USER,page.getOffset(),page.getLimit());
        if (userList!=null){
            for (Map map:userList){
                User u = (User) map.get("user");
                boolean hasFollowed = followServie.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_USER,u.getId());
                map.put("hasFollowed",hasFollowed);
            }
        }
        model.addAttribute("users",userList);
        return "/site/follower";
    }

}
