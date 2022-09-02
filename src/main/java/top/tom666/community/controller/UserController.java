package top.tom666.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import top.tom666.community.annotation.LoginRequired;
import top.tom666.community.entity.User;
import top.tom666.community.service.UserService;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.HostHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author: liujisen
 * @date： 2022-08-30
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${community.path.upload}")
    private String uploadPath;
    @Autowired
    private HostHolder hostHolder;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.path.domain}")
    private String domain;

    @LoginRequired
    @GetMapping("/setting")
    public String getSetting(){
        return "/site/setting";
    }

    @PostMapping("/upload")
    public String uploadHeader(MultipartFile imageFile, Model model){
        if (imageFile == null){
            model.addAttribute("error","您还没选择照片：");
            return "/site/setting";
        }
        String filename = imageFile.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件的格式不正确");
            return "/site/setting";
        }
        //随机文件名
        filename = CommunityUtils.generateUUID()+suffix;
        log.info("uploadPath为：{}",uploadPath);
        File dest = new File(uploadPath + "/" + filename);
        try {
            imageFile.transferTo(dest);
        } catch (IOException e) {
            log.error("上传文件失败",e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常",e);
        }
        User user= hostHolder.getUser();
        String header = domain + contextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(),header);
        return "redirect:/index";
    }
    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable String fileName, HttpServletResponse response){
        fileName = uploadPath +"/"+fileName;
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应结果
        response.setContentType("image/"+suffix);
        try (
                OutputStream os = response.getOutputStream();
                FileInputStream fileInputStream = new FileInputStream(fileName);
        ) {
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.error("读取头像失败: " + e.getMessage());
        }
    }


}
