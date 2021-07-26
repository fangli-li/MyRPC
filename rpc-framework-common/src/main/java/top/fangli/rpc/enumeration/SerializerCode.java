package top.fangli.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fangli
 */

@AllArgsConstructor
@Getter
public enum SerializerCode {
    // JSON 的序号
    JSON(1);

    private final int code;
}
