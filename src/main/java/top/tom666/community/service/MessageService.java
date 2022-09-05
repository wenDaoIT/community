package top.tom666.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.tom666.community.dao.MessageMapper;
import top.tom666.community.entity.Message;

import java.util.List;

/**
 * @author: liujisen
 * @date： 2022-09-05
 */
@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    public List<Message> findConversations(int userId, int offset, int limit){
        return messageMapper.selectConversions(userId, offset, limit);
    }
    public int findConversationsCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId,int offset,int limit){
        return messageMapper.selectLetter(conversationId, offset, limit);
    }

    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(int userId,String conversationId){
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

}
