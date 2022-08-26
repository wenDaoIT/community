package top.tom666.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.tom666.community.entity.User;
import top.tom666.community.service.UserService;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.Constant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author: liujisen
 * @date： 2022-08-25
 */

@Controller
public class LoginController implements Constant {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(){

        return "/site/register";
    }
    @PostMapping("/register")
    public String register(Model model, User user){
        Map<String,Object> map = userService.register(user);
        if (map == null || map.isEmpty()){
            model.addAttribute("msg","注册成功，请去邮箱激活");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }
        model.addAttribute("usernameMsg",map.get("usernameMsg"));
        model.addAttribute("passwordMsg",map.get("passwordMsg"));
        model.addAttribute("emailMsg",map.get("emailMsg"));
        return "/site/register";
    }

    @GetMapping("/activation/{userId}/{code}")
    public String activation(Model model,
                             @PathVariable("userId") int userId,
                             @PathVariable("code") String code){
        int result=userService.activation(userId,code);
        if (result == ACTIVATION_SUCCESS){
            model.addAttribute("msg","已成功激活");
            model.addAttribute("target","/login");
        }else if(result == ACTIVATION_REPEAT){
            model.addAttribute("msg","该账号已经激活");
            model.addAttribute("target","index");
        }else{
            model.addAttribute("msg","激活失败，激活码不正确");
            model.addAttribute("target","");
        }
        return "/site/operate-result";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "/site/login";
    }

    @GetMapping("/path/set")
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("code",CommunityUtils.generateUUID());
        cookie.setPath("/community/student");
        //默认存放在浏览器中，如果设置了过期时间，会存放在硬盘中
        cookie.setMaxAge(60 * 10);
        response.addCookie(cookie);
        return  "set cookie";
    }


    @GetMapping("/session/set")
    public String setSession(HttpSession httpSession){
        httpSession.setAttribute("name",200);
        httpSession.setAttribute("msg","session");
        return "set session";
    }

    @GetMapping("/session/get")
    public String getSession(HttpSession httpSession){
        System.out.println(httpSession.getAttribute("name"));
        System.out.println(httpSession.getId());
        return httpSession.getId();
    }


}
