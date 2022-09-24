package top.tom666.community.util.exception;

/**
 * @author: liujisen
 * @date： 2022-09-21
 */

public enum CommonEnum implements BaseErrorInfoInterface{

    SUCCESS(200,"请求成功"),
    BODY_NOT_MATCH(400,"请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH(401,"请求错误!"),
    NOT_FOUND(404, "未找到该资源!"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误!"),
    SERVER_BUSY(503,"服务器正忙，请稍后再试!");

    /**错误码*/
    private final int code;
    /**
     * 错误消息
     */
    private final String message;

    CommonEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 错误码
     */
    @Override
    public int getCode() {
        return code;
    }

    /**
     * 错误描述
     */
    @Override
    public String getMessage() {
        return message;
    }
}
