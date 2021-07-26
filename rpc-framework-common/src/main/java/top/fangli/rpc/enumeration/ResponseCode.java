package top.fangli.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fangli
 */

@Getter
@AllArgsConstructor
public enum ResponseCode {
    //响应状态码
    SUCCESS(200, "调用方法成功"),
    FAIL(500, "调用方法失败"),
    NOT_FOUND_METHOD(500, "未找到指定方法"),
    NOT_FOUND_CLASS(500, "未找到指定类");

    private final int code;
    private final String message;
}
