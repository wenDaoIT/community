package top.tom666.community.util;

/**
 * @author: liujisen
 * @date： 2022-09-07
 */

public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITTY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";


    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITTY_LIKE + SPLIT +entityType + SPLIT +entityId;
    }
    public static String getPrefixUserLike(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

}
