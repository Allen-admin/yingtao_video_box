package com.k365.video_manager_api.channelController;


import com.k365.channel_service.ChannelService;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.dto.ChannelDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Allen
 * @date 2019/11/05
 * @description：
 */
@RestController
@RequestMapping(value = {"/channel"})
@Api(tags = {"推广渠道接口"})
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "新增渠道")
    public String add(@RequestBody @Validated @ApiParam(value = "新增渠道") ChannelDTO channelDTO) {
        channelService.add(channelDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = {"/list"})
    @ApiOperation(value = "获取渠道列表")
    public String list(@RequestBody ChannelDTO channelDTO) {
        return ResultFactory.buildSuccessResult(channelService.find(channelDTO));
    }

    @PostMapping(value = {"/update"})
    @ApiOperation(value = "修改渠道")
    public String update(@RequestBody ChannelDTO channelDTO) {
        channelService.update(channelDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除渠道")
    public String remove(@PathVariable("id") String id) {
        channelService.removeById(id);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/search")
    @ApiOperation(value = "通过渠道编号和渠道名搜索")
    public String searchPage(@RequestBody ChannelDTO channelDTO) {
        return ResultFactory.buildSuccessResult(channelService.searchPage(channelDTO));
    }

/*    @PostMapping("/search")
    @ApiOperation(value = "通过渠道编号和渠道名搜索")
    public String search(@RequestBody ChannelDTO channelDTO) {
        return ResultFactory.buildSuccessResult(channelService.search(channelDTO));
    }*/


    @PostMapping("/searchDate")
    @ApiOperation(value = "通过时间段搜索")
    public String searchDate(@RequestBody ChannelDTO channelDTO) {
        return ResultFactory.buildSuccessResult(channelService.searchDate(channelDTO));
    }

    @PostMapping("/count")
    @ApiOperation(value = "查询")
    public String count(@RequestBody ChannelDTO channelDTO) {
        return ResultFactory.buildSuccessResult(channelService.count(channelDTO));
    }

    /*
    @PostMapping("/list")
    @ApiOperation(value = "渠道列表")
    public String searchPage(@RequestBody ChannelDTO channelDTO) {
        return ResultFactory.buildSuccessResult(channelService.searchPage(channelDTO));
    }*/

/*    @PostMapping("/findPage")
    @ApiOperation(value = "查询渠道和数量")
    public String findPage(@RequestBody ChannelDTO channelDTO) {
        return ResultFactory.buildSuccessResult(channelService.findPage(channelDTO));
    }*/


    @GetMapping("/findAll")
    @ApiOperation(value = "查询")
    public String findAll() {
        return ResultFactory.buildSuccessResult(channelService.findAll());
    }


}
