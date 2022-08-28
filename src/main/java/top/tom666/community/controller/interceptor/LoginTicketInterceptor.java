package top.tom666.community.controller.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.tom666.community.entity.LoginTicket;
import top.tom666.community.service.UserService;
import top.tom666.community.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author: liujisen
 * @date： 2022-08-28
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从request拿cookie
        String ticket = CookieUtil.getValue(request,"ticket");
        if (ticket != null) {
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            if (loginTicket != null && loginTicket.getStatus() != 0
                    && loginTicket.getExpired().after(new Date())) {
                return true;
            }

        }
        return false;
    }
}
