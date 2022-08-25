package top.tom666.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.tom666.community.entity.User;

/**
 * @author: liujisen
 * @date： 2022-08-23
 */
@Repository
@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByUsername(String name);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id,int status);

    int updatePassword(int id,String passward);
}