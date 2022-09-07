package top.tom666.community.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import top.tom666.community.util.CommunityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: liujisen
 * @date： 2022-09-06
 */
@ControllerAdvice(annotations = Controller.class)
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        log.error("服务器发生异常",e.getMessage());
        //遍历堆栈信息并储存
        for (StackTraceElement element:e.getStackTrace()){
            log.error(element.toString());
        }
         String requestHeader = httpRequest.getHeader("x-requested-with");
        if (requestHeader.equals("XMLHttpRequest")){
            response.setContentType("application/plain;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtils.getJSONString(1,"服务器异常"));
        }else {
            response.sendRedirect(httpRequest.getContextPath() + "/error");
        }
    }
}
