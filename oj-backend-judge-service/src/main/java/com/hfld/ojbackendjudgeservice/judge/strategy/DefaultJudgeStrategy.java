package com.hfld.ojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.hfld.ojbackendmodel.model.codesandbox.JudgeInfo;
import com.hfld.ojbackendmodel.model.dto.question.JudgeCase;
import com.hfld.ojbackendmodel.model.dto.question.JudgeConfig;
import com.hfld.ojbackendmodel.model.entity.Question;
import com.hfld.ojbackendmodel.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {
    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        long memory = judgeInfo.getMemory();
        long time = judgeInfo.getTime();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.WAITING;
        judgeInfoMessageEnum = judgeInfoMessageEnum.WRONG_ANSWER;

        JudgeInfo judgeInfoRsponse = new JudgeInfo();
        judgeInfoRsponse.setMemory(memory);
        judgeInfoRsponse.setTime(time);


        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = judgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoRsponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoRsponse;
        }
        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoRsponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoRsponse;
            }
        }
        // 判断题目的限制是否符合要求
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        long needTimeLimit = judgeConfig.getTimeLimit();
        long needMemoryLimit = judgeConfig.getMemoryLimit();
        if (memory > needMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoRsponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoRsponse;
        }
        if (time > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoRsponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoRsponse;
        }

        judgeInfoRsponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoRsponse;
    }
}
