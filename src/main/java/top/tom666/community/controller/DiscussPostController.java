package top.tom666.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.tom666.community.entity.DiscussPost;
import top.tom666.community.entity.Page;
import top.tom666.community.entity.User;
import top.tom666.community.service.CommentService;
import top.tom666.community.service.DiscussPostService;
import top.tom666.community.service.UserService;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.Constant;
import top.tom666.community.util.HostHolder;

import java.util.Date;

/**
 * @author: liujisen
 * @date： 2022-09-03
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements Constant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

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

    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable int discussPostId, Model model, Page page){
        DiscussPost post = discussPostService.findDiscussPostByid(discussPostId);
        model.addAttribute("post",post);

        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);

        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setCount(post.getCommentCount());

        commentService.findCommentsByEntity(ENTITY_TYPE_POST,post.getId(),page.getOffset(),page.getLimit());


        return "site/discuss-detail";
    }

}
