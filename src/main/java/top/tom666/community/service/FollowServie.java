package top.tom666.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import top.tom666.community.util.RedisKeyUtil;

import javax.annotation.Resource;

/**
 * @author: liujisen
 * @date： 2022-09-10
 */
@Service
public class FollowServie {
    @Resource
    private RedisTemplate redisTemplate;

    public void follow(int userId,int entityType , int entityId){

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String folloeeKey  = RedisKeyUtil.getPrefixFollee(userId,entityType);
                String followerKey = RedisKeyUtil.getPrefixFoller(entityType, entityId);
                //开启事务
                operations.multi();
                operations.opsForZSet().add(folloeeKey,entityId,System.currentTimeMillis());
                operations.opsForZSet().add(followerKey,userId,System.currentTimeMillis());
                //结束事务
                return operations.exec();
            }
        });
    }
    public void unfollow(int userId,int entityType , int entityId){

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String folloeeKey  = RedisKeyUtil.getPrefixFollee(userId,entityType);
                String followerKey = RedisKeyUtil.getPrefixFoller(entityType, entityId);
                //开启事务
                operations.multi();
                operations.opsForZSet().remove(folloeeKey,entityId);
                operations.opsForZSet().remove(followerKey,userId);
                //结束事务
                return operations.exec();
            }
        });
    }

    //查询关注的实体的数量
    public long findFolloweeCount(int userId, int entityType){
        String followeeKey = RedisKeyUtil.getPrefixFollee(userId ,entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }
    //查询某个实体的粉丝数量
    public long fingFollowerCount(int entityType , int entityId){
        String followerKey = RedisKeyUtil.getPrefixFoller(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }
    //查询是否关注某个实体
    public boolean hasFollowed(int userId , int entityType , int entityId){
        String followeeKey = RedisKeyUtil.getPrefixFollee(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey,entityId) != null;
    }

}
