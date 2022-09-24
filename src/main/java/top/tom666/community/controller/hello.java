package top.tom666.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.tom666.community.util.CommunityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liujisen
 * @date： 2022-08-21
 */
@Controller
public class hello {
    @Autowired
    private TransactionTemplate transactionTemplate;

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "环境搭建完成";
    }


    @GetMapping("/list")
    @ResponseBody
    public String getStudent(
            @RequestParam(name = "current",required = false,defaultValue = "1") int pageNo,
            @RequestParam(name = "limit",required = false,defaultValue = "10") int limit){
        System.out.println(pageNo);
        System.out.println(limit);
        return "student list 1";
    }
    @RequestMapping(value = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public int getStudent1(@PathVariable("id") int id){

        return id;
    }


    @PostMapping("/student")
    @ResponseBody
    public String postStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return name;
    }

    @GetMapping("teacher")
    public ModelAndView getTeacher(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","张三");
        modelAndView.addObject("age","16");
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    @GetMapping("school")
    public String getSchool(Model model){
        model.addAttribute("name","BEIJING");
        model.addAttribute("age","100");
        return "/demo/view";
    }

    @GetMapping("emps")
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        List<Map<String,Object>> mapList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("age",10);
        mapList.add(map);
        map =new HashMap<>();
        map.put("name","李四");
        map.put("age",13);
        mapList.add(map);
        return mapList;
    }

    @GetMapping("/student/getcookie")
    @ResponseBody
    public String getCookie(@CookieValue("code") String code){
        return code;
    }


    @PostMapping("/ajax")
    @ResponseBody
    public String testAjax(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtils.getJSONString(200,"ok");

    }

    /**
     * @return spring事务测试
     */
    private Object transation(){
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                System.out.println("这里实现需要回滚的业务代码");
                return null;
            }
        });
    }


    @RequestMapping("doCompute/{n1}/{n2}")
    @ResponseBody
    public String doCompute(@PathVariable  Integer n1,
                            @PathVariable Integer n2){

        Integer result=n1/n2;
            return "Result is "+result;
//        try{
//            Integer result=n1/n2;
//            return "Result is "+result;
//        }catch(ArithmeticException e){
//            return "exception is "+e.getMessage();
//        }
    }

//    @ExceptionHandler(ArithmeticException.class)
//    @ResponseBody
//    public String doHandleArithmeticException(ArithmeticException e){
//        e.printStackTrace();
//        return "计算过程中出现了异常，异常信息为"+e.getMessage();
//    }

}
