package com.k365.video_manager_api.controller;


import com.k365.user_service.UserFeedbackService;
import com.k365.video_base.model.dto.UserFeedbackDTO;
import com.k365.video_base.model.po.UserFeedback;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_common.annotation.SysLogs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户反馈意见表 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/user/user-feedback")
@Api(tags = {"用户反馈管理"})
public class UserFeedbackController {

    @Autowired
    private UserFeedbackService userFeedbackService;

    @ApiOperation(value = "分页查询所有用户反馈")
    @PostMapping("/list")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String list(@RequestBody UserFeedbackDTO userFeedbackDTO){
        return ResultFactory.buildSuccessResult(userFeedbackService.find(userFeedbackDTO));
    }


    @ApiOperation(value = "修改用户反馈状态")
    @PostMapping("/update")
    @SysLogs("修改用户反馈状态")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody UserFeedback userFeedback ){
        userFeedbackService.updateStatusById(userFeedback);
        return ResultFactory.buildSuccessResult();
    }

    @ApiOperation(value = "删除用户反馈")
    @PostMapping("/remove/{id}")
    @SysLogs("删除用户反馈")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") String id ){
        userFeedbackService.remove(id);
        return ResultFactory.buildSuccessResult();
    }

}

