package com.hfld.ojbackendjudgeservice.judge.codesandbox.impl;


import com.hfld.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.hfld.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.hfld.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.hfld.ojbackendmodel.model.codesandbox.JudgeInfo;
import com.hfld.ojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.hfld.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱（仅为跑通业务流程）
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();

        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
