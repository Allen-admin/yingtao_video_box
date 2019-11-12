package com.k365.video_manager_api.channelController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.k365.channel_service.VUserChannelService;
import com.k365.user_service.UserService;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.so.UserChannelSO;
import com.k365.video_base.model.so.VUserChannelSO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Allen
 * @date 2019/11/05
 * @description：
 */
@RestController
@RequestMapping(value = {"/UserCannel"})
@Api(tags = {"用户渠道接口"})
public class VUserChannerController {

    @Autowired
    private VUserChannelService vUserChannelService;


    @Autowired
    private UserService userService;

    @PostMapping("/findList")
    @ApiOperation(value = "查询渠道和数量,总数")
    public String findList(@RequestBody VUserChannelSO vUserChannelSO) {
        return ResultFactory.buildSuccessResult(vUserChannelService.findList(vUserChannelSO));
    }

    @PostMapping("/search")
    @ApiOperation(value = "搜索渠道信息")
    public String search(@RequestBody VUserChannelSO vUserChannelSO) {
        return ResultFactory.buildSuccessResult(vUserChannelService.search(vUserChannelSO));
    }

    @PostMapping("/searchDate")
    @ApiOperation(value = "搜索时间段信息")
    public String searchDate(@RequestBody VUserChannelSO vUserChannelSO) {
        return ResultFactory.buildSuccessResult(vUserChannelService.searchPage(vUserChannelSO));
    }

    @PostMapping("/searchList")
    @ApiOperation(value = "搜索编号，渠道名，时间搜信息")
    public String searchList(@RequestBody VUserChannelSO vUserChannelSO) {
        return ResultFactory.buildSuccessResult(vUserChannelService.searchList(vUserChannelSO));
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "查询所有用户等级")
    public String findAll() {
        return ResultFactory.buildSuccessResult(vUserChannelService.findAll());
    }

    @PostMapping("/list")
    @ApiOperation(value = "用户相关信息")
    public String list(@RequestBody VUserChannelSO vUserChannelSO) {
        return ResultFactory.buildSuccessResult(vUserChannelService.findUser(vUserChannelSO));
    }

    @GetMapping(value = "/all-user")
    @ApiOperation(value = "查询总用户量")
    public String allUser() {
        return ResultFactory.buildSuccessResult(userService.count(new QueryWrapper<User>()));

    }

    @PostMapping("/searchUser")
    @ApiOperation(value = "根据条件检索用户相关信息")
    public String searchUser(@RequestBody UserChannelSO userChannelSO) {
        return ResultFactory.buildSuccessResult(vUserChannelService.searchUser(userChannelSO));
    }

}
