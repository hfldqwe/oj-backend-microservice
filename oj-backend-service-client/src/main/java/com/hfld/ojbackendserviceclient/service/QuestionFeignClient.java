package com.hfld.ojbackendserviceclient.service;

import com.hfld.ojbackendmodel.model.entity.Question;
import com.hfld.ojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author lenovo
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-01-12 15:27:58
*/
@FeignClient(name = "oj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    @PostMapping("/question_submit/get/id")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
}
