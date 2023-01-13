package com.my9z.study.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 延迟消息时间级别枚举
 * @author: kim
 * @createTime: 2023-01-13  09:45
 */
@Getter
@AllArgsConstructor
public enum DelayTimeLevelEnum {

    SECOND_1(1, "1s"),
    SECOND_5(2, "5s"),
    SECOND_10(3, "10s"),
    SECOND_30(4, "30s"),
    MINUTE_1(5, "1m"),
    MINUTE_2(6, "2m"),
    MINUTE_3(7, "3m"),
    MINUTE_4(8, "4m"),
    MINUTE_5(9, "5m"),
    MINUTE_6(10, "6m"),
    MINUTE_7(11, "7m"),
    MINUTE_8(12, "8m"),
    MINUTE_9(13, "9m"),
    MINUTE_10(14, "10m"),
    MINUTE_20(15, "20m"),
    MINUTE_30(16, "30m"),
    HOUR_1(17, "1h"),
    HOUR_2(18, "2h"),
    ;
    private final int level;
    private final String time;
}
