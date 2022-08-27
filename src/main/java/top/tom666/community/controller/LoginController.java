package top.tom666.community.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.tom666.community.config.KaptchaConfig;
import top.tom666.community.entity.User;
import top.tom666.community.service.UserService;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.Constant;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author: liujisen
 * @date： 2022-08-25
 */

@Controller
@Slf4j
public class LoginController implements Constant {
    @Autowired
    private UserService userService;

    @Autowired
    private Producer producer;

    @Value("${server.servlet.context-path}")
    private String contextPath;


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

    /**
     * @param response
     * @param httpSession 验证码属于敏感数据，跨请求
     */
    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response,HttpSession httpSession){
        //生成验证码
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);

        httpSession.setAttribute("kaptcha",text);
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            log.error("响应验证码失败",e.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(Model model,String username,
                        String password,String code,HttpSession httpSession,HttpServletResponse response){
            String kaptcha = (String) httpSession.getAttribute("kaptcha");

        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equals(code)){
            model.addAttribute("codeMsg","验证码错误");
            return  "/site/login";
        }

        Map<String , Object> map = userService.login(username,password);
        if (map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(1000 * 60 *10);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }

    }

    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket){

        userService.logout(ticket);
        return "redirect:/login";
    }




}
