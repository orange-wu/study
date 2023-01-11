package com.my9z.study.enums;

import com.my9z.study.exception.WczRocketMQException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 错误码枚举
 * @author: kim
 * @createTime: 2023-01-11  17:25
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    NAME_SERVER_OR_GROUP_IS_EMPTY("0001", "nameServerAddress or producerGroup is empty"),
    GROUP_IS_EMPTY("0002", "producerGroup is empty"),
    ;

    private final String code;

    private final String msg;

    public WczRocketMQException buildException() {
        return new WczRocketMQException(this.code, this.msg);
    }

}
