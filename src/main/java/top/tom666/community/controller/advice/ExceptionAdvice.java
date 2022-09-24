package top.tom666.community.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.tom666.community.util.CommunityUtils;
import top.tom666.community.util.ResultBody;
import top.tom666.community.util.exception.BizException;
import top.tom666.community.util.exception.CommonEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: liujisen
 * @date： 2022-09-06
 */
@ControllerAdvice(annotations = Controller.class)
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(ArithmeticException.class)
    public String doHandleArithmeticException(ArithmeticException e){
//        e.printStackTrace();
        //遍历堆栈信息并储存
        for (StackTraceElement element:e.getStackTrace()){
            log.error(element.toString());
        }
        return  "计算过程中出现了异常，异常信息为"+e.getMessage();
    }

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public  ResultBody bizExceptionHandler(HttpServletRequest req, BizException e){
        log.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return ResultBody.error(e.getErrorCode(),e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("发生空指针异常！原因是:",e);
        return ResultBody.error(CommonEnum.BODY_NOT_MATCH);
    }

    //处理其他异常
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
