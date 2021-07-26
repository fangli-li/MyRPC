package top.fangli.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fangli
 */

@AllArgsConstructor
@Getter
public enum PackageType {

    /**
     * 请求数据包
     */
    REQUEST_PACK(0),

    /**
     * 响应数据包
     */
    RESPONSE_PACK(1);

    private final int code;
}
