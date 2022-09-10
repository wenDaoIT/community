package top.tom666.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.tom666.community.entity.User;
import top.tom666.community.service.LikeService;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.HostHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: liujisen
 * @date： 2022-09-07
 */
@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType,int entityId){
        User user = hostHolder.getUser();
        likeService.like(user.getId(),entityType,entityId);

        long likeCount = likeService.selectLikeCount(entityType,entityId);

        int likeStatus = likeService.findUserLikeStatus(user.getId(),entityType,entityId);
        Map<String , Object> map = new HashMap<>();

        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);
        return CommunityUtils.getJSONString(0,null,map);

    }

}
