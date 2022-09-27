package top.tom666.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.tom666.community.entity.User;
import top.tom666.community.service.LikeService;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.HostHolder;
import top.tom666.community.util.RedisKeyUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: liujisen
 * @date： 2022-09-07
 */
@Controller
public class LikeController {
    @Resource
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;
    @Resource
    private RedisTemplate redisTemplate;
    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType,int entityId,int entityUserId){
        User user = hostHolder.getUser();
        likeService.like(user.getId(),entityType,entityId,entityUserId);

        long likeCount = likeService.selectLikeCount(entityType,entityId);

        int likeStatus = likeService.findUserLikeStatus(user.getId(),entityType,entityId);
        Map<String , Object> map = new HashMap<>();

        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);
        //计算分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey,entityId);
        return CommunityUtils.getJSONString(0,null,map);
    }

}
