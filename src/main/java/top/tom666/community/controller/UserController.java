package top.tom666.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import top.tom666.community.annotation.LoginRequired;
import top.tom666.community.service.UserService;
import top.tom666.community.util.CommunityUtils;

import java.io.File;

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

    @Value("${community.upload.path}")
    private String uploadPath;

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
        File file = new File(uploadPath + "/" + filename);
//        imageFile.transferTo(file);
    }


}
