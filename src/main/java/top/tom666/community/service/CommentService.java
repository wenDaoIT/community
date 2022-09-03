package top.tom666.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.tom666.community.dao.CommentMapper;
import top.tom666.community.entity.Comment;

import java.util.List;

/**
 * @author: liujisen
 * @date： 2022-09-03
 */
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }
    public int findCommentCount(int entityType, long entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

}
