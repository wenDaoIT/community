package top.tom666.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.tom666.community.entity.DiscussPost;
import top.tom666.community.entity.User;
import top.tom666.community.service.DiscussPostService;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.HostHolder;

import java.util.Date;

/**
 * @author: liujisen
 * @date： 2022-09-03
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/add")
    @ResponseBody
    public String addDisscussPost(String title,String content){
        User user = hostHolder.getUser();
        if (user == null){
            return CommunityUtils.getJSONString(403,"你还没有登陆");
        }
        final DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);
        //报错将来统一处理
        return CommunityUtils.getJSONString(0,"发送成功");
    }

}
