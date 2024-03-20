package com.hfld.ojbackendquestionservice.controller.inner;

import com.hfld.ojbackendmodel.model.entity.Question;
import com.hfld.ojbackendmodel.model.entity.QuestionSubmit;
import com.hfld.ojbackendquestionservice.service.QuestionService;
import com.hfld.ojbackendquestionservice.service.QuestionSubmitService;
import com.hfld.ojbackendserviceclient.service.QuestionFeignClient;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @GetMapping("/get/id")
    @Override
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    @GetMapping("/question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @PostMapping("/question_submit/get/id")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}
