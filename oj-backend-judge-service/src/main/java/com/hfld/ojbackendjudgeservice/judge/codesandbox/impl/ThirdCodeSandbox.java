package com.hfld.ojbackendjudgeservice.judge.codesandbox.impl;


import com.hfld.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.hfld.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.hfld.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用网上现成的代码沙箱）
 */
public class ThirdCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
