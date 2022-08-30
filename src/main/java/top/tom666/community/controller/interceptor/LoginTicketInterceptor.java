package top.tom666.community.controller.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.tom666.community.entity.LoginTicket;
import top.tom666.community.entity.User;
import top.tom666.community.service.UserService;
import top.tom666.community.util.CookieUtil;
import top.tom666.community.util.HostHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author: liujisen
 * @date： 2022-08-28
 */
@Component
@Slf4j
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder holder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从request拿cookie
        String ticket = CookieUtil.getValue(request,"ticket");
        if (ticket != null) {
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            if (loginTicket != null && loginTicket.getStatus() == 0
                    && loginTicket.getExpired().after(new Date())) {
                //根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                //在本次请求中持有user 考虑线程隔离
                holder.setUser(user);
            }
        }
        log.info("preHandle被调用");
        return true;
    }

    //在模版引擎调用之前，将user存放到model中
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = holder.getUser();
        if (user != null && modelAndView != null){
            modelAndView.addObject("loginUser",user);
        }
        log.info("posthandle被调用");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion被调用，当前线程结束，清除线程池数据");
        holder.clearUser();
    }
}
