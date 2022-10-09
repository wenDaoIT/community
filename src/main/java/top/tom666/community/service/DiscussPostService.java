package top.tom666.community.service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import top.tom666.community.dao.DiscussPostMapper;
import top.tom666.community.entity.DiscussPost;
import top.tom666.community.util.SensitiveFilter;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: liujisen
 * @date： 2022-08-24
 */
@Slf4j
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.posts.max-size}")
    private int maxSize;
    @Value("${caffeine.posts.expire-seconds}")
    private int expriedTime;

    @PostConstruct
    public void init(){
        //初始化帖子缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expriedTime, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Nullable
                    @Override
                    public List<DiscussPost> load(String key) throws Exception {
                        if (key == null || key.length() == 0){
                            throw new IllegalArgumentException("参数错误");
                        }
                        String[] params = key.split(":");
                        if (params == null ||params.length != 2){
                            throw new IllegalArgumentException("参数错误");
                        }
                        int offset = Integer.parseInt(params[0]);
                        int limit = Integer.parseInt(params[1]);
                        // 这里可以先访问二级缓存 redis->DB
                        return discussPostMapper.selectDiscussPosts(0,offset,limit,1);
                    }
                });
        //初始化帖子总数缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expriedTime,TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Nullable
                    @Override
                    public Integer load(Integer integer) throws Exception {
                        if (integer == null){
                            throw new IllegalArgumentException("参数错误");
                        }
                        log.debug("load post list from DB.");
                        return discussPostMapper.selectDiscussPostRows(integer);
                    }
                });
    }


    //caffine核心接口Cache,LoadingCache

    //帖子列表缓存
    private LoadingCache<String,List<DiscussPost>> postListCache;
    //帖子总数缓存
    private LoadingCache<Integer,Integer> postRowsCache;


    public List<DiscussPost> getDiscussPosts(int userId,int offset,int size,int orderModel){
        if (userId ==0 && orderModel ==1){
            return postListCache.get(offset + ":"+size);
        }
        log.debug("load post list from DB.");
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(userId, offset, size,orderModel);
        return discussPosts;
    }
    public int findDiscussPostRow(int userId){
        if (userId == 0){
            return postRowsCache.get(userId);
        }
        log.debug("load post listRows from DB.");
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

    public int updateCommentCount(int id, int count){
        return discussPostMapper.updateCommentCount(id,count);
    }

    public int updataScore(int id,double score) {
        return discussPostMapper.updateScore(id,score);
    }
}
