package top.tom666.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.tom666.community.entity.Comment;

import java.util.List;

/**
 * @author: liujisen
 * @date： 2022-09-03
 */
@Mapper
@Repository
public class CommentMapper {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

}
