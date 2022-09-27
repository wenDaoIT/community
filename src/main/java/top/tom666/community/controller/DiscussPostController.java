package top.tom666.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.tom666.community.entity.Comment;
import top.tom666.community.entity.DiscussPost;
import top.tom666.community.entity.Page;
import top.tom666.community.entity.User;
import top.tom666.community.service.CommentService;
import top.tom666.community.service.DiscussPostService;
import top.tom666.community.service.UserService;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.Constant;
import top.tom666.community.util.HostHolder;
import top.tom666.community.util.RedisKeyUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/add")
    @ResponseBody
    public String addDisscussPost(String title,String content){
        User user = hostHolder.getUser();
        if (user == null){
            return CommunityUtils.getJSONString(403,"你还没有登陆");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPost.setStatus(0);
        discussPost.setType(0);
        discussPost.setCommentCount(0);
        discussPostService.addDiscussPost(discussPost);
        //计算分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey,discussPost.getId());
        //报错将来统一处理
        return CommunityUtils.getJSONString(0,"发送成功");
    }

    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable int discussPostId, Model model, Page page){
        DiscussPost post = discussPostService.findDiscussPostByid(discussPostId);
        model.addAttribute("post",post);

        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);
        //评论分页列表
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setCount(post.getCommentCount());
        //评论：给帖子的评论
        //回复：给评论的评论
        //获取评论
        List<Comment> commentList=commentService.findCommentsByEntity(ENTITY_TYPE_POST,
                post.getId(),page.getOffset(),page.getLimit());
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        if (commentList != null){
            for (Comment comment:
                 commentList) {
                Map<String, Object> commentvo = new HashMap<>();
                commentvo.put("comment",comment);
                commentvo.put("user",userService.findUserById(comment.getUserId()));

                //查询回复
                List<Comment> replyList =commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT,comment.getId(),0,Integer.MAX_VALUE);
                List<Map<String,Object>> replyLists = new ArrayList<>();
                if (replyList != null){
                    for (Comment reply: replyList
                         ) {
                        Map<String,Object> replyvo = new HashMap<>();
                        replyvo.put("comment",reply.getContent());
                        replyvo.put("user",userService.findUserById(reply.getUserId()));
                        //回复目标 target如果不为空，
                        User target=reply.getTargetId() ==0 ? null : userService.findUserById(reply.getTargetId());
                        if (target !=null){
                            replyvo.put("target",target);
                            replyLists.add(replyvo);
                        }
                    }
                }
                commentvo.put("replys",replyLists);
                //回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());

                commentvo.put("replyCount",replyCount);
                commentVoList.add(commentvo);
            }
        }

        model.addAttribute("comments",commentVoList);
        return "site/discuss-detail";
    }

}
