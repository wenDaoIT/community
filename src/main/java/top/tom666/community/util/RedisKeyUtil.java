package top.tom666.community.util;

/**
 * @author: liujisen
 * @date： 2022-09-07
 */

public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITTY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";

    private static final String PREFIX_FOLLEE = "follee";
    private static final String PREFIX_FOLLER = "foller";

    private static final String PREFIX_KAPTCHA= "kaptcha";

    private static final String PREFIX_TICKET = "ticket";

    private static final String PREFIX_USER = "user";

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITTY_LIKE + SPLIT +entityType + SPLIT +entityId;
    }
    public static String getPrefixUserLike(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /** 获取储存用户关注的实体的key
     * @param userId 当前登录的用户id
     * @param entityType 用户关注的实体id
     * @return follee:userId:entityType -> zset(entityId,now)
     */
    public static String getPrefixFollee(int userId,int entityType){
        return PREFIX_FOLLEE + SPLIT + userId + SPLIT + entityType;
    }

    //某个实体拥有的粉丝
    public static String getPrefixFoller(int entityType,int entityId){
        return PREFIX_FOLLER +SPLIT+entityType +SPLIT +entityId;
    }
    //验证码
    public static String getPrefixKaptcha(String UUID){
        return PREFIX_KAPTCHA + SPLIT + UUID;
    }

    public static String getPrefixTicket(String ticket){
        return PREFIX_TICKET +SPLIT+ticket;
    }
    public static String getPrefixUser(int userId){
        return PREFIX_USER + SPLIT +userId;
    }
}
