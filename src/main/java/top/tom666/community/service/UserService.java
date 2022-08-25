package top.tom666.community.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.context.Context;
import top.tom666.community.dao.UserMapper;
import top.tom666.community.entity.User;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.Constant;
import top.tom666.community.util.MailClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author: liujisen
 * @date： 2022-08-24
 */
@Service
public class UserService implements Constant {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private UserMapper userMapper;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.path.domain}")
    private String domain;

    @Autowired
    private TemplateEngine templateEngine;

    public User findUserById(int userId){
        return userMapper.selectById(userId);
    }

    public Map<String, Object> register(User user){
        Map<String,Object> result = new HashMap<>();
        if (user == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())){
            result.put("usernameMsg","账号不能为空！");
            return result;
        }
        if (StringUtils.isBlank(user.getPassword())){
            result.put("passwordMsg","密码不能为空！");
            return result;
        }
        //先去数据库查询
        User user1 = userMapper.selectByUsername(user.getUsername());
        if (user1 != null){
            result.put("usernameMsg","该账号已存在");
            return result;
        }
        //验证邮箱是否存在
        user1 = userMapper.selectByEmail(user.getEmail());
        if (user1 != null){
            result.put("emailMsg","该邮箱已存在");
            return result;
        }
        //注册用户
        user.setSalt(CommunityUtils.generateUUID().substring(0,5));
        user.setPassword(CommunityUtils.md5(user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtils.generateUUID());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        //insert会回填自动生成的id
        String url =domain + contextPath + "/activation" +"/"+ user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);

        String content = templateEngine.process("/mail/activation",context);

        mailClient.sendMail(user.getEmail(),"激活账号",content);

        return result;
    }


    /**
     * @param userId
     * @param code 激活码
     * @return 判断激活码是否正确
     */
    public int activation(int userId, String code){
        User user = userMapper.selectById(userId);

        if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }
        return ACTIVATION_FAIL;

    }
}
