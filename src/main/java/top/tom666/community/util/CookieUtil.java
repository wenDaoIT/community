package top.tom666.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: liujisen
 * @date： 2022-08-28
 */

public class CookieUtil {

    public static String getValue(HttpServletRequest request,String name){
        if (request == null || name ==null){
            throw new IllegalArgumentException("参数为空");
        }
        Cookie[] cookie = request.getCookies();
        if (cookie != null){
            for (Cookie cookie1:
                    cookie) {
//                String cookie1Value = cookie1.getValue();
                if (cookie1.getName().equals(name)){
                    return cookie1.getValue();
                }
            }
        }
        return null;
    }

}
