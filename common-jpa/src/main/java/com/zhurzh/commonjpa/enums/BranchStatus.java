package com.zhurzh.commonjpa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BranchStatus {
    MENU("/menu"),
    START("/start"),
    ORDER("/order"),
    CHECK_ORDER("/order_service"),
    PRICE_LIST("/price_list"),
    FAQ("/faq"),
    SEA_REM("/sea_rem");

    private String path;
}
