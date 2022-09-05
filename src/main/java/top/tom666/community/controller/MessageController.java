package top.tom666.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.tom666.community.entity.Message;
import top.tom666.community.entity.Page;
import top.tom666.community.entity.User;
import top.tom666.community.service.MessageService;
import top.tom666.community.service.UserService;
import top.tom666.community.util.HostHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liujisen
 * @date： 2022-09-05
 */
@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page){
        //配置分页
        page.setLimit(5);
        page.setPath("/letter/list");
        User user = hostHolder.getUser();
        page.setCount(messageService.findConversationsCount(user.getId()));
        //会话列表
        List<Message> messageList = messageService.findConversations(
                user.getId(),page.getOffset(),page.getLimit());

        List<Map<String , Object>> conversations = new ArrayList<>();
        if (messageList != null){
            for (Message message:messageList){
                Map<String,Object> map= new HashMap<>();
                map.put("conversation",message);
                map.put("letterCount",messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount",messageService.findLetterUnreadCount(user.getId(),message.getConversationId()));
                int targetId  = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target",userService.findUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations",conversations);

        //查询用户所有未读私信总数
        int letterCount = messageService.findLetterUnreadCount(user.getId(),null);

        model.addAttribute("letterCount",letterCount);
        return "/site/letter";


    }
    @GetMapping("/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable String conversationId,Model model,Page page){
        //分页查询
        page.setPath("/letter/detail/"+conversationId);
        page.setLimit(5);
        page.setCount(messageService.findLetterCount(conversationId));
        List<Message> messageList = messageService.findLetters(conversationId,page.getOffset(),page.getLimit());
        List<Map<String , Object>> letters = new ArrayList<>();
        if (messageList!= null){
            for (Message letter : messageList){
                Map<String , Object> map = new HashMap<>();
                map.put("letter",letter);
                map.put("fromUser",userService.findUserById(letter.getFromId()));

                letters.add(map);
            }
        }
        model.addAttribute("letters",letters);
        //获取发来私信的对象
        model.addAttribute("target",getLetterTarget(conversationId));
        return "/site/letter-detail";
    }

    private User getLetterTarget(String conversationId){
        String[] id = conversationId.split("_");
        int one = Integer.parseInt(id[0]);
        int two = Integer.parseInt(id[1]);
        return hostHolder.getUser().getId() == one ?
                userService.findUserById(two) : userService.findUserById(one);
    }

}
