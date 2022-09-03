package top.tom666.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import top.tom666.community.dao.DiscussPostMapper;
import top.tom666.community.entity.DiscussPost;
import top.tom666.community.util.SensitiveFilter;

import java.util.List;

/**
 * @author: liujisen
 * @date： 2022-08-24
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> getDiscussPosts(int userId,int offset,int size){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(userId, offset, size);
        return discussPosts;
    }
    public int findDiscussPostRow(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public int addDiscussPost(DiscussPost discussPost){
        if (discussPost == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转义html标记语言 防止注入
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        //过滤敏感词
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));

        return discussPostMapper.insertDiscussPost(discussPost);
    }


    public DiscussPost findDiscussPostByid(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }
}
