package top.fangli.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fangli
 */

@AllArgsConstructor
@Getter
public enum SerializerCode {

    // KRYO
    KRYO(0),
    // JSON
    JSON(1);

    private final int code;
}
