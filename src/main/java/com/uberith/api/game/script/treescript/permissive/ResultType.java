package com.uberith.api.game.script.treescript.permissive;

import com.uberith.api.util.StringUtils;

public enum ResultType {
    MET,
    NOT_MET,
    EXPIRED;


    public static ResultType getResult(boolean value) {
        return value ? ResultType.MET : ResultType.NOT_MET;
    }

    public String getName() {
        return StringUtils.toTitleCase(name());
    }
}
