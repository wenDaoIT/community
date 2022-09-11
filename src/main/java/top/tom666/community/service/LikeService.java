package top.tom666.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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
    public void like(int userId,int entityType,int entityId,int entityUserId){
//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
//        Boolean isnumber = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
//        if (isnumber){
//            redisTemplate.opsForSet().remove(entityLikeKey,userId);
//        }
//        redisTemplate.opsForSet().add(entityLikeKey,userId);

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);

                String userLikeKey = RedisKeyUtil.getPrefixUserLike(entityUserId);
                //判断当前用户是否点赞
                Boolean isnumber = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
                //开启事务
                operations.multi();
                if (isnumber){
                    operations.opsForSet().remove(entityLikeKey,userId);
                    operations.opsForValue().decrement(userLikeKey);
                }else {
                    operations.opsForSet().add(entityLikeKey,userId);
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });
    }


    public long selectLikeCount(int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }


    public int findUserLikeStatus(int userId,int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1 : 0 ;
    }

    /**
     * 查询某个用户获得的赞
     * @param userId 当前用户的id
     * @return
     */
    public int findUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.getPrefixUserLike(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0:count.intValue();
    }

}
