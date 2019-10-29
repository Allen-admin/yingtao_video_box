package com.k365.video_manager_api.controller;


import com.k365.manager_service.NoticeService;
import com.k365.video_base.common.AppDisplayTypeEnum;
import com.k365.video_base.model.dto.NoticeDTO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/spread/notice")
@Api(tags = "公告管理")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/list/{app}")
    @ApiOperation(value = "查询所有公告")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String list(@PathVariable("app") Integer app) {
        return ResultFactory.buildSuccessResult(noticeService.findAll(app));
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加公告")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody NoticeDTO noticeDTO) {
        noticeService.add(noticeDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改公告")
    @SysLogs("修改公告")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody NoticeDTO noticeDTO, @PathVariable("id") Integer id) {
        noticeDTO.setId(id);
        noticeService.update(noticeDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除公告")
    @SysLogs("删除公告")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove( @PathVariable("id") Integer id) {
        noticeService.remove(id);
        return ResultFactory.buildSuccessResult();
    }

    @ApiOperation(value = "获取公告展示位置")
    @GetMapping("/get/show-position")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getShowNoticePosition() {
        return ResultFactory.buildSuccessResult(AppDisplayTypeEnum.getAdtEnumsByType(AppDisplayTypeEnum.NOTICE));
    }

}

