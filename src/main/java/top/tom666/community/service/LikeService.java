package top.tom666.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.tom666.community.util.RedisKeyUtil;

/**
 * @author: liujisen
 * @date： 2022-09-07
 */
@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**对某个实体点赞的动作
     * @param userId
     * @param entityType
     * @param entityId
     */
    public void like(int userId,int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        Boolean isnumber = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if (isnumber){
            redisTemplate.opsForSet().remove(entityLikeKey,userId);
        }
        redisTemplate.opsForSet().add(entityLikeKey,userId);

    }


    public long selectLikeCount(int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }


    public int findUserLikeStatus(int userId,int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1 : 0 ;
    }




}
