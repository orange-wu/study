package com.my9z.study.common.enums;

import com.my9z.study.common.exception.WczRocketMQException;
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
    PRODUCER_GROUP_IS_EMPTY("0002", "producerGroup is empty"),
    PRODUCER_INIT_ERROR("0003", "producer start error"),
    BUILD_MESSAGE_ERROR("0004", "build message error,topic or msgBody is null"),
    UNIQUE_KEY_IS_EMPTY("005","uniqueKey is empty"),
    CONSUMER_MODE_IS_UN_KNOW("006","error consumer mode"),
    CONSUMER_INIT_ERROR("0007", "consumer start error"),

    ;

    private final String code;

    private final String msg;

    public WczRocketMQException buildException() {
        return new WczRocketMQException(this.code, this.msg);
    }

}
