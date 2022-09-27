package top.tom666.community.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import top.tom666.community.entity.DiscussPost;
import top.tom666.community.service.DiscussPostService;
import top.tom666.community.service.LikeService;
import top.tom666.community.util.Constant;
import top.tom666.community.util.RedisKeyUtil;
import top.tom666.community.util.exception.BizException;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * @author: liujisen
 * @date： 2022-09-26
 */
@Slf4j
public class PostScoreRefreshJob implements Job, Constant {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private DiscussPostService discussPostService;
    @Resource
    private LikeService likeService;

    private static final Date epoch;
    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
        } catch (ParseException e) {
            throw new BizException(-1,"初始化纪元失败",e);
        }
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String redisKey = RedisKeyUtil.getPostScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);
        if (operations.size() == 0){
            log.info("没有需要刷新的帖子");
        }
        log.info("开始刷新");
        while (operations.size()>0){
            this.refresh((Integer) operations.pop());
        }
        log.info("任务结束");
    }

    private void refresh(int postId) {
        DiscussPost post = discussPostService.findDiscussPostByid(postId);
        boolean wonderful = post.getStatus() == 1;

        int comment = post.getCommentCount();

        int likeCount = likeService.findEntityLikeCount(postId);

        double w = (wonderful ? 75 : 0) + comment * 10 +likeCount *2;

        double score = Math.log10(Math.max(w,1)) + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 *3600 *24);

        discussPostService.updataScore(postId,score);
    }
}
