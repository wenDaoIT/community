package top.tom666.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.tom666.community.dao.UserMapper;
import top.tom666.community.entity.User;

/**
 * @author: liujisen
 * @date： 2022-08-24
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int userId){
        return userMapper.selectById(userId);
    }

}
