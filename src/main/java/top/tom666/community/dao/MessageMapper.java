package top.tom666.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.tom666.community.entity.Message;

import java.util.List;

/**
 * @author: liujisen
 * @date： 2022-09-05
 */
@Mapper
public interface MessageMapper {
    /**    //查询当前用户的会话列表，返回最新的一条数据
     * @param userId 当前用户id
     * @param offset 当前位置
     * @param limit 每次查询限制
     * @return
     */
    List<Message> selectConversions(@Param("userId") int userId, int offset, int limit);

    /**
     * @param userId
     * @return 查询当前用户的会话总数
     */
    int selectConversationCount(int userId);

    /**
     * //查询某个会话 包含的所有私信集合 详情页面
     * @param conversationId 拼接的对话id
     * @param offset 页
     * @param limit 条数
     * @return
     */
    List<Message> selectLetter(String conversationId,int offset,int limit);

    /**
     *
     * @param conversationId
     * @return 查询某个会话中包含的私信数量
     */
    int selectLetterCount(String conversationId);

    /**
     * @param userId to用户的id
     * @param conversationId 单个会话未读私信数量
     * @return 查询未读私信数量
     */
    int selectLetterUnreadCount(int userId,String conversationId);

}
