package com.k365.video_manager_api.controller;

import com.k365.global.CommonService;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gavin
 * @date 2019/8/13 12:01
 * @description：
 */
@Api(tags = {"文件处理控制器"})
@RequestMapping("/file")
@RestController
public class FileHandleController {

    @Autowired
    private CommonService commonService;

    @PostMapping("/uploadFile")
    @ApiOperation(value = "文件上传")
    public String uploadFile(MultipartFile file,String oldFilePath){
        return ResultFactory.buildSuccessResult(commonService.uploadFile(file,oldFilePath));

    }

}
