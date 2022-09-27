package top.tom666.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.tom666.community.entity.Comment;
import top.tom666.community.service.CommentService;
import top.tom666.community.util.HostHolder;
import top.tom666.community.util.RedisKeyUtil;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: liujisen
 * @date： 2022-09-04
 */
@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    @Resource
    private RedisTemplate redisTemplate;
    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable String discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        //计算分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey,comment.getId());
        return "redirect:/discuss/detail/" + discussPostId;
    }


}
