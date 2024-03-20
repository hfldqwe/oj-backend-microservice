package com.hfld.ojbackendmodel.model.codesandbox;

import lombok.Data;

@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 小号内存（KB）
     */
    private long memory;

    /**
     * 消耗时间（ms）
     */
    private long time;
}
