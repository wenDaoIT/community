package top.tom666.community.util;

/**
 * @author: liujisen
 * @date： 2022-08-29
 */

import org.springframework.stereotype.Component;
import top.tom666.community.entity.User;

/**
 * 持有用户对象，代替session
 */
@Component
public class HostHolder {
    private ThreadLocal<User> userThreadLocal = new ThreadLocal<>();


    public void setUser(User user){
        userThreadLocal.set(user);
    }
    public User getUser(){
        return userThreadLocal.get();
    }

    public void clearUser(){
        userThreadLocal.remove();
    }



}
