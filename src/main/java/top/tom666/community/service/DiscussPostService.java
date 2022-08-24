package top.tom666.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.tom666.community.dao.DiscussPostMapper;
import top.tom666.community.entity.DiscussPost;

import java.util.List;

/**
 * @author: liujisen
 * @date： 2022-08-24
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> getDiscussPosts(int userId,int offset,int size){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(userId, offset, size);
        return discussPosts;
    }
    public int findDiscussPostRow(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

}
