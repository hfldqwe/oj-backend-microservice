package com.hfld.ojbackendjudgeservice.controller.inner;

import com.hfld.ojbackendjudgeservice.judge.JudgeService;
import com.hfld.ojbackendmodel.model.entity.QuestionSubmit;
import com.hfld.ojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    JudgeService judgeService;

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    @Override
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
