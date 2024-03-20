package com.hfld.ojbackendjudgeservice.judge.codesandbox;

import com.hfld.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.hfld.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
