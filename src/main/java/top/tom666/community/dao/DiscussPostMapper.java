package top.tom666.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.tom666.community.entity.DiscussPost;

import java.util.List;

/**
 * @author: liujisen
 * @date： 2022-08-24
 */
@Mapper
@Repository
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);

    int selectDiscussPostRows(@Param("userId") int userId);


    int insertDiscussPost(DiscussPost discussPost);
}
