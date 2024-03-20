package com.hfld.ojbackendjudgeservice.judge;

import com.hfld.ojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.hfld.ojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.hfld.ojbackendjudgeservice.judge.strategy.JudgeContext;
import com.hfld.ojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.hfld.ojbackendmodel.model.codesandbox.JudgeInfo;
import com.hfld.ojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理(简化调用)
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (language.equals("java")) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
