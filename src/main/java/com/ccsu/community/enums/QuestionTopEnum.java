package com.ccsu.community.enums;

/**
 * @author 华华
 */

public enum  QuestionTopEnum {
    //1置顶
    IS_TOP(1),
    //0不置顶
    NO_TOP(0);

    private int type;

    QuestionTopEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
