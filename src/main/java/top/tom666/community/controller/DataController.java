package top.tom666.community.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.tom666.community.service.DataService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: liujisen
 * @date： 2022-09-24
 */
@Controller
public class DataController {
    @Resource
    private DataService dataService;

    @RequestMapping(value = "/data",method = {RequestMethod.GET,RequestMethod.POST})
    public String getDataPage(){
        return "/admin/data";
    }

    //统计网站UV
    @PostMapping("/data/uv")
    public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                        Model model){
        long uv = dataService.calculateUV(start, end);
        model.addAttribute("uvResult",uv);
        model.addAttribute("uvStartDate",start);
        model.addAttribute("uvEndDate",end);
        return "forward:/data";
    }

    //统计网站DAU
    @PostMapping("/data/dau")
    public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                         Model model) {
        long dau = dataService.calcuteDAU(start, end);
        model.addAttribute("dauStartDate", start);
        model.addAttribute("dauEndDate", end);
        model.addAttribute("dauResult", dau);
        return "forward:/data";
    }


}
