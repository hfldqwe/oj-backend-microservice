package com.hfld.ojbackendquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hfld.ojbackendcommon.common.ErrorCode;
import com.hfld.ojbackendcommon.constant.CommonConstant;
import com.hfld.ojbackendcommon.exception.BusinessException;
import com.hfld.ojbackendcommon.utils.SqlUtils;
import com.hfld.ojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.hfld.ojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.hfld.ojbackendmodel.model.entity.Question;
import com.hfld.ojbackendmodel.model.entity.QuestionSubmit;
import com.hfld.ojbackendmodel.model.entity.User;
import com.hfld.ojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.hfld.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.hfld.ojbackendmodel.model.vo.QuestionSubmitVO;
import com.hfld.ojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.hfld.ojbackendquestionservice.rabbitmq.MyMessageProducer;
import com.hfld.ojbackendquestionservice.service.QuestionService;
import com.hfld.ojbackendquestionservice.service.QuestionSubmitService;
import com.hfld.ojbackendserviceclient.service.JudgeFeignClient;
import com.hfld.ojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lenovo
 * @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
 * @createDate 2024-01-12 15:30:10
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private MyMessageProducer myMessageProducer;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return 提交记录 id
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //  校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }

        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        long userId = loginUser.getId();
        // 每个用户串行提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WATTING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交失败");
        }

        Long questionSubmitId = questionSubmit.getId();
        // 不再直接调用判题服务，直接将题目提交id发送给消息队列就行
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", String.valueOf(questionSubmitId));
        // 执行判题服务
//        CompletableFuture.runAsync(() -> {
//            judgeFeignClient.doJudge(questionSubmitId);
//        });
        return questionSubmitId;
    }

    /**
     * 获取查询包装类 （用户根据哪些字段查询，根据前端传来的请求对象，得到mybatis框架支持的查询QueryWrapper类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：仅本人和管理员能看见自己(提交userId和登录用户id不同)提交的代码
        Long userId = loginUser.getId();
        if (userId != questionSubmit.getUserId() && !userFeignClient.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().
                map(questionSubmit -> {
                    return getQuestionSubmitVO(questionSubmit, loginUser);
                })
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

}




