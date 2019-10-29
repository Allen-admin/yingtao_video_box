package com.k365.video_manager_api.controller;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.exception.OceanException;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.vo.UmengResultVo;
import com.umeng.uapp.param.UmengUappGetAllAppDataParam;
import com.umeng.uapp.param.UmengUappGetAllAppDataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author ALLEN
 * @date 2019/9/27 19:07
 * @description：
 */
@RestController
@RequestMapping(value = {"/home/dataview"})
@Api(tags = {"首页视图"})
@Slf4j
public class ViewController {

    final static String APPKEY = "7658331";

    final static String SECKEY = "R2fGZybSj3JY";

    final static String UMENGNET  ="gateway.open.umeng.com";


    /*    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;*/

    /*@GetMapping(value = "/all-user")
    @ApiOperation(value = "查询总用户量")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String allUser() {
        return ResultFactory.buildSuccessResult(userService.count(new QueryWrapper<User>()));

    }

    @GetMapping(value = "/recommend-user")
    @ApiOperation(value = "查询推广注册用户数")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String recommendUser() {
        int count = userService.count(new QueryWrapper<User>().isNotNull("recommender_id").ne("recommender_id", ""));
        return ResultFactory.buildSuccessResult(count);
    }

    @GetMapping(value = "/register4month")
    @ApiOperation(value = "查询当月用户注册量")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String register4Month() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fristDayOfMonth = LocalDateTime.of(LocalDate.of(now.getYear(), now.getMonth(), 1), LocalTime.MIN);
        int count = userService.count(new QueryWrapper<User>().ge("register_time", fristDayOfMonth));
        return ResultFactory.buildSuccessResult(count);
    }

    @GetMapping(value = "/register4day")
    @ApiOperation(value = "查询当天用户注册量")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String register4Day() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fristDayOfMonth = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        int count = userService.count(new QueryWrapper<User>().ge("register_time", fristDayOfMonth));
        return ResultFactory.buildSuccessResult(count);
    }

    @PostMapping(value = "/video4month")
    @ApiOperation(value = "查询当月播放量前十的视频")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String video4Month(@RequestBody VideoDTO videoDTO) {
        IPage<Video> page = videoService.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                new QueryWrapper<Video>().orderByDesc("play_count_for_month"));

        return ResultFactory.buildSuccessResult(page.getRecords());
    }

    @PostMapping(value = "/video4week")
    @ApiOperation(value = "查询当周播放量前十的视频")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String video4Week(@RequestBody VideoDTO videoDTO) {
        IPage<Video> page = videoService.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                new QueryWrapper<Video>().orderByDesc("play_count_for_week"));

        return ResultFactory.buildSuccessResult(page.getRecords());
    }

    @PostMapping(value = "/video4day")
    @ApiOperation(value = "查询当天播放量前十的视频")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String video4Day(@RequestBody VideoDTO videoDTO) {
        IPage<Video> page = videoService.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                new QueryWrapper<Video>().orderByDesc("play_count_for_day"));

        return ResultFactory.buildSuccessResult(page.getRecords());
    }

    @PostMapping(value = "/video-play-sum")
    @ApiOperation(value = "查询总播放量前十的视频")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String videoPlaySum(@RequestBody VideoDTO videoDTO) {
        IPage<Video> page = videoService.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                new QueryWrapper<Video>().orderByDesc("play_sum"));

        return ResultFactory.buildSuccessResult(page.getRecords());
    }

    @PostMapping(value = "/video-save-sum")
    @ApiOperation(value = "查询总下载量前十的视频")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String videoSaveSum(@RequestBody VideoDTO videoDTO) {
        IPage<Video> page = videoService.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                new QueryWrapper<Video>().orderByDesc("save_sum"));

        return ResultFactory.buildSuccessResult(page.getRecords());
    }*/

    @GetMapping(value = "/today")
    @ApiOperation(value = "今日统计图数据")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String todayData() {

        //调用友盟接口获取数据
        UmengUappGetAllAppDataParam param = new UmengUappGetAllAppDataParam();
        ApiExecutor apiExecutor = new ApiExecutor(APPKEY, SECKEY);
        apiExecutor.setServerHost(UMENGNET);
        // 测试环境只支持http
        // param.getOceanRequestPolicy().setUseHttps(false);
        UmengResultVo vo = null;
        UmengUappGetAllAppDataResult result=null;
        try {
            result = apiExecutor.execute(param);

        } catch (OceanException e) {
            log.error("调用友盟接口异常：errorCode={} ,errorMessage={}" , e.getErrorCode() , e.getErrorMessage());
        }
        if(result.getAllAppData().length>0){
            vo = UmengResultVo.builder().build();
            vo.setActivityUsers(result.getAllAppData()[0].getTodayActivityUsers());
            vo.setLaunches(result.getAllAppData()[0].getTodayLaunches());
            vo.setNewUsers(result.getAllAppData()[0].getTodayNewUsers());
            vo.setTotalUsers(result.getAllAppData()[0].getTotalUsers());

        }
        return ResultFactory.buildSuccessResult(vo);

    }

    @GetMapping(value = "/yesterday")
    @ApiOperation(value = "昨日统计图数据")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String yesterdayData() {

        //调用友盟接口获取数据
        UmengUappGetAllAppDataParam param = new UmengUappGetAllAppDataParam();
        ApiExecutor apiExecutor = new ApiExecutor(APPKEY, SECKEY);
        apiExecutor.setServerHost(UMENGNET);
        // 测试环境只支持http
        // param.getOceanRequestPolicy().setUseHttps(false);
        UmengResultVo vo = null;
        UmengUappGetAllAppDataResult result=null;
        try {
            result = apiExecutor.execute(param);

        } catch (OceanException e) {
            log.error("调用友盟接口异常：errorCode={} ,errorMessage={}" , e.getErrorCode() , e.getErrorMessage());

        }

        if(result.getAllAppData().length>0){
            vo = UmengResultVo.builder().build();
            vo.setActivityUsers(result.getAllAppData()[0].getYesterdayActivityUsers());
            vo.setLaunches(result.getAllAppData()[0].getYesterdayLaunches());
            vo.setNewUsers(result.getAllAppData()[0].getYesterdayNewUsers());
            vo.setTotalUsers(result.getAllAppData()[0].getTotalUsers());
        }

        return ResultFactory.buildSuccessResult(vo);

    }

}
