package com.hfld.ojbackendserviceclient.service;


import com.hfld.ojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 判题服务
 */
@FeignClient(name = "oj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {
    // judgeService.doJudge(questionSubmitId)

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
