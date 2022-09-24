package top.tom666.community.util.exception;

/**
 * @author: liujisen
 * @date： 2022-09-21
 */

public interface BaseErrorInfoInterface {
    /** 错误码*/
    int getCode();

    /** 错误描述*/
    String getMessage();
}
