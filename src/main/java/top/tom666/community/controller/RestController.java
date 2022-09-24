package top.tom666.community.controller;

import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.tom666.community.entity.User;
import top.tom666.community.util.exception.BizException;

/**
 * @author: liujisen
 * @date： 2022-09-20
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@Controller
public class RestController {


    @PostMapping("/login")
    public void login(@RequestBody User user){
        if (user.getUsername() == null){
            throw new BizException(-1,"用户姓名不能为空！");
        }
    }

    @PutMapping("/put")
    @ResponseBody
    public void update(User user){
        //这里制造一个空指针异常
        String str = null;
        str.equals("123");
    }

    @GetMapping("/get")
    @ResponseBody
    public void delete(){
        Integer.parseInt("abc123");
        throw new BizException("服务器内部错误");
    }
}
