package top.tom666.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.tom666.community.entity.DiscussPost;
import top.tom666.community.entity.Page;
import top.tom666.community.entity.User;
import top.tom666.community.service.DiscussPostService;
import top.tom666.community.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liujisen
 * @date： 2022-08-24
 */
@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    public DiscussPostService discussPostService;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page){
        //方法调用之前，model会自动实例化page并注入，所以不用添加
        page.setCount(discussPostService.findDiscussPostRow(0));
        page.setPath("/index");

        List<DiscussPost> postList= discussPostService.getDiscussPosts(0,page.getOffset(),page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (!postList.isEmpty()) {
            for (DiscussPost post:postList){
                Map<String, Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }


}
