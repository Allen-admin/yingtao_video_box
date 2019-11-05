package com.k365.user_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.SysConfParamService;
import com.k365.new_manager_service.ManagerEmployeeService;
import com.k365.user_service.*;
import com.k365.video_base.common.CustomAttr;
import com.k365.video_base.common.UserContext;
import com.k365.video_base.common.VideoContants;
import com.k365.video_base.mapper.UserMapper;
import com.k365.video_base.model.dto.UserDTO;
import com.k365.video_base.model.po.ManagerEmployee;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserLevel;
import com.k365.video_base.model.po.UserVideoCollection;
import com.k365.video_base.model.so.UserSO;
import com.k365.video_base.model.vo.*;
import com.k365.video_common.constant.*;
import com.k365.video_common.exception.GeneralException;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.handler.fastdfs.FastDFSFile;
import com.k365.video_common.handler.fastdfs.FastDFSHelper;
import com.k365.video_common.handler.sms.AbstractSMSProvider;
import com.k365.video_common.handler.sms.NeteaseSMS;
import com.k365.video_common.handler.sms.SMSFactory;
import com.k365.video_common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    //token 过期时间 单位：秒
    public static final long EXPIRE_TIME = 7 * 24 * 60 * 60;

    @Autowired
    private RedisUtil cache;

    @Autowired
    private CustomAttr attr;

    @Autowired
    @Lazy(true)
    private UserLevelService userLevelService;

    @Autowired
    private UserSaveVideoRecordService userSaveVideoRecordService;

    @Autowired
    private UserViewingRecordService userViewingRecordService;

    @Autowired
    private UserVideoChannelService userVideoChannelService;

    @Autowired
    @Lazy(true)
    private UserTaskRecordService userTaskRecordService;

    @Autowired
    private SysConfParamService sysConfParamService;

    @Autowired
    private UserVideoCollectionService userVideoCollectionService;

    @Autowired
    @Lazy
    private ManagerEmployeeService managerEmployeeService;

    @Override
    public Map<String, Object> visitorAutoRegister(ServletRequest request, UserDTO userDTO) {
        //获取App类型
        final AppTypeEnum appType = HttpUtil.getAppType(request) == null ? AppTypeEnum.XIAO_AI : HttpUtil.getAppType(request);

        String macAddr = userDTO.getMacAddr();

        //TODO mac未加密 暂时注释
        //AES解密
        try {
            macAddr = AESCipher.aesDecryptString(macAddr);

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //mac地址前添加app标识，多个app用户数据隔离
        macAddr = StringUtils.join(appType.getCode(), "-", macAddr);
        userDTO.setMacAddr(macAddr);


        User user = this.getOne(new QueryWrapper<User>().eq("mac_addr", macAddr));

        boolean newUser = false;
        if (user == null) {
            newUser = true;
            UserLevel level = userLevelService.getOne(new QueryWrapper<UserLevel>().eq("level", 1));

            user = User.builder().macAddr(userDTO.getMacAddr()).userLevel(1).viewingCount(level.getViewingCount())
                    .saveCount(level.getSaveCount()).status(UserStatusEnum.NORMAL.code()).registerTime(new Date())
                    .usedViewingCount(0).recommendCount(0).awardSaveCount(0).awardViewingCount(0).usedSaveCount(0)
                    .nickname("樱桃" + RandomKeyUtil.getRandomStr(6).toUpperCase())
                    .appType(appType.getKey()).build();
        } else if (user.getStatus() != UserStatusEnum.NORMAL.code()) {
            throw new ResponsiveException("当前账号状态异常，禁止登陆");
        }


        //判断是否为会员
        if (user.getPhone() != null && !user.getPhone().equals("")) {
            if (user.getVipEndTime() != null) {
                int compare = user.getVipEndTime().compareTo(new Date());
                if (compare == -1 || compare == 0) {
                    user.setVipType(2);
                } else {
                    user.setVipType(1);
                }
            } else {
                user.setVipType(2);
            }
        } else {
            user.setVipType(0);
        }

        //判断用户VIP到期时间是否已经到期  如果已经到期则修改VIP类型为2
       /* if (user.getVipType() == null) {
            user.setVipType(0);
        }
        if (user.getVipEndTime() != null) {
            int compare = user.getVipEndTime().compareTo(new Date());
            if (compare == -1||compare == 0) {
                this.doUpdateUser(User.builder().vipType(2).id(user.getId()).build());
            }else{
                if(user.getPhone()!=null&&!user.getPhone().equals("")){
                    this.doUpdateUser(User.builder().vipType(1).id(user.getId()).build());
                }else{
                    this.doUpdateUser(User.builder().vipType(2).id(user.getId()).build());
                }
            }
        }*/

        String ip = IPUtil.getClientIp(WebUtils.toHttp(request));
        user.setLastLoginIp(ip);
        //获取上次登出时间
        Long lastLoginOutTime = userDTO.getLastLoginOutTime();
        //获取上次登录时间
        Date lastLoginTime = user.getLastLoginTime();
        //登录时前端传一个上次登出时间，用登出时间减去上次登录时间，存到数据库一个新的字段。
        if (lastLoginOutTime != null && lastLoginTime != null) {
            long time = lastLoginOutTime - lastLoginTime.getTime(); //最近使用时长
            time = TimeUnit.MILLISECONDS.toMinutes(time);
            user.setLastTime(time);
        }
        //把上次登录登录时间设置为当前时间
        user.setLastLoginTime(new Date());


        if (this.saveOrUpdate(user)) {
            if (newUser || StringUtils.isBlank(user.getRecommendCode())) {
                if (this.update(new UpdateWrapper<User>().eq("id", user.getId()).set("recommend_code", user.getId()))) {
                    user.setRecommendCode(user.getId());
                }
            }

            final String uId = user.getId();
            final boolean isNeedCreateQR = newUser || StringUtils.isBlank(user.getSpreadQrcodeUrl());
            final String spreadQrcodeUrl = user.getSpreadQrcodeUrl();
            //另起一个线程来生成推广二维码图片
            new Thread(() -> {
                try {
                    if (isNeedCreateQR || !verifySpreadQr(spreadQrcodeUrl, appType)) {
                        this.createSpreadQrcodeImg(uId, spreadQrcodeUrl, appType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            String cacheKey = Constants.CACHE_VISITOR_USER_INFO + user.getId();
            cache.set(cacheKey, user, DateUtil.getSurplusSecondOfToday());

            Map<String, Object> result = new HashMap<>();
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            String sign = JwtUtil.sign(user.getId(), VideoContants.VIDEO_SITE_USER_CIPHERTEXT, currentTimeMillis, EXPIRE_TIME);
            UserInfoVO userInfo = getUserInfo(user);
            userInfo.setVipType(user.getVipType());
            userInfo.setVipEndTime(user.getVipEndTime());
            result.put("token", sign);
            result.put("userInfo", userInfo);
            return result;
        } else {
            throw new ResponsiveException("游客登录失败！");
        }

    }

    @Override
    public boolean verifySpreadQr(String spreadQrcodeUrl, AppTypeEnum appType) {
        if (StringUtils.isBlank(spreadQrcodeUrl)) {
            return false;
        }

        String[] dfsPath = spreadQrcodeUrl.split("/", 5);
        InputStream inputStream = FastDFSHelper.downloadFile(dfsPath[3], dfsPath[4]);
        String decodeQrcodeUrl;
        try {
            decodeQrcodeUrl = QRcodeUtil.decode(inputStream);
        } catch (Exception e) {
            log.error("解析推广二维码路径出错", e);
            return false;
        }

        SysParamValueNameEnum sysParamValueName = Objects.equals(appType.getKey(), AppTypeEnum.HEI_MEI.getKey()) ?
                SysParamValueNameEnum.HEIMEI_PAGE_URl : SysParamValueNameEnum.XIAOAI_PAGE_URl;

        String pageUrl = (String) sysConfParamService.getValByValName(sysParamValueName);
        return StringUtils.isNotBlank(decodeQrcodeUrl) && decodeQrcodeUrl.startsWith(pageUrl);
    }


    @Override
    public String createSpreadQrcodeImg(String uId, String spreadQrcodeUrl, AppTypeEnum appType) {
        SysParamValueNameEnum pageParam = Objects.equals(appType.getKey(), AppTypeEnum.HEI_MEI.getKey()) ?
                SysParamValueNameEnum.HEIMEI_PAGE_URl : SysParamValueNameEnum.XIAOAI_PAGE_URl;

        SysParamValueNameEnum logoParam = Objects.equals(appType.getKey(), AppTypeEnum.HEI_MEI.getKey()) ?
                SysParamValueNameEnum.HEIMEI_LOGO_URL : SysParamValueNameEnum.XIAOAI_LOGO_URL;

        String logoUrl = (String) sysConfParamService.getValByValName(logoParam);

        String downloadAppUrl = (String) sysConfParamService.getValByValName(pageParam);
        String path = null;
        if (StringUtils.isNotBlank(downloadAppUrl)) {
            downloadAppUrl = StringUtils.join(downloadAppUrl, "?code=", uId);

            String[] dfsPath = logoUrl.split("/", 5);
            InputStream inputStream = null;
            ByteArrayOutputStream outputStream = null;
            try {
                inputStream = FastDFSHelper.downloadFile(dfsPath[3], dfsPath[4]);
                outputStream = new ByteArrayOutputStream();
                QRcodeUtil.encode(downloadAppUrl, inputStream, outputStream);
                if (outputStream.size() > 0) {
                    String[] uploadFile = FastDFSHelper.uploadFile(new FastDFSFile().setContent(outputStream.toByteArray()).setExt("png"));

                    if (uploadFile == null || uploadFile.length < 2) {
                        throw new ResponsiveException("文件保存失败");
                    }

                    path = StringUtils.join(attr.getFastdfsServer(), "/", uploadFile[0], "/", uploadFile[1]);
                    //删除旧的推广二维码
                    if (StringUtils.isNotBlank(spreadQrcodeUrl)) {
                        String[] dfsPath2 = spreadQrcodeUrl.split("/", 5);
                        FastDFSHelper.deleteFile(dfsPath2[3], dfsPath2[4]);
                    }
                    this.doUpdateUser(User.builder().id(uId).spreadQrcodeUrl(path).build());

                }
            } catch (Exception e) {
                throw new GeneralException("生成推广二维码失败！", e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error("生成二维码，关闭输入流失败,e:{}", e.getMessage());
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        log.error("生成二维码，关闭输出流失败,e:{}", e.getMessage());
                    }
                }
            }
        }

        return path;

    }

    @Override
    public VideoViewingInfoVO getViewingCount() {
        User currentUser = UserContext.getCurrentUser();

        VideoViewingInfoVO vo = new VideoViewingInfoVO();

        if (currentUser != null) {
            vo = vo.setId(currentUser.getId()).setPhone(RegsUtil.shadowPhone(currentUser.getPhone()))
                    .setRecommendCount(currentUser.getRecommendCount()).setSaveCount(currentUser.getSaveCount())
                    .setUsedSaveCount(currentUser.getUsedSaveCount()).setVipEndTime(currentUser.getVipEndTime())
                    .setViewingCount(currentUser.getViewingCount()).setVipType(currentUser.getVipType())
                    .setUsedViewingCount(currentUser.getUsedViewingCount());

            UserLevel nextLevel = userLevelService.getOne(new QueryWrapper<UserLevel>().eq("level", currentUser.getUserLevel() + 1));

            if (nextLevel != null) {
                vo.setNextLevelCount(nextLevel.getRecommendCount());
            }

            //查看观影次数 从系统参数表中获取标识 判断是否为1   为1则返回-1无限
            Map<String, Object> paramValByName = sysConfParamService.findParamValByName(SysParamNameEnum.VIEW_INFINITE_VALUE);
            String value = paramValByName.get(SysParamValueNameEnum.INFINITE_VALUE.code()).toString();
            if (value.equals("1")) {
                vo.setViewingCount(-1);
            }

        }
        return vo;
    }

    @Override
    public BaseListVO<UserVO> search(UserSO userSO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userSO.getId())) {
            queryWrapper.like("id", userSO.getId());
        }
        if (StringUtils.isNotBlank(userSO.getMacAddr())) {
            queryWrapper.like("mac_addr", userSO.getMacAddr());
        }
        if (StringUtils.isNotBlank(userSO.getPhone())) {
            queryWrapper.like("phone", userSO.getPhone());
        }
        if (StringUtils.isNotBlank(userSO.getNickname())) {
            queryWrapper.like("nickname", userSO.getNickname());
        }

        IPage<User> page = this.page(new Page<User>().setSize(userSO.getPageSize()).setCurrent(userSO.getPage()), queryWrapper);
        List<User> list = page.getRecords();
        List<UserVO> voList = new ArrayList<>();
        if (!ListUtils.isEmpty(list)) {
            list.forEach(user -> voList.add(UserVO.builder().id(user.getId()).macAddr(user.getMacAddr()).phone(user.getPhone())
                    .registerTime(user.getRegisterTime()).userLevel(user.getUserLevel()).vipEndTime(user.getVipEndTime()).nickname(user.getNickname()).
                            appType(user.getAppType()).status(user.getStatus()).build()));
        }
        return new BaseListVO<UserVO>().setList(voList).setTotal(page.getTotal());
    }

    @Override
    public void sendVerifyCode(String mobile) {
        if (!RegsUtil.validatePhone(mobile)) {
            throw new ResponsiveException("请输入正确的手机号");
        }

        SMSFactory sms = new NeteaseSMS();
        String verifyCode = sms.getSMSProvider().getVerifyCode();
        String verifyCodeKey = StringUtils.join(Constants.CACHE_USER_VERIFY_CODE, mobile, ":", verifyCode);
        int statusCode;

        try {
            statusCode = sms.getSMSProvider().sendCode(mobile, verifyCode);
            System.out.println(statusCode + "====");
        } catch (Exception e) {
            throw new GeneralException("发送短信验证码失败", e);
        }
        if (AbstractSMSProvider.SUCCESS_CODE == statusCode) {
            //将验证码存入redis
            cache.set(verifyCodeKey, verifyCode, AbstractSMSProvider.ACTIVE_TIME);
        } else {
            throw new ResponsiveException("发送短信验证码失败");
        }

    }


    @Override
    public void callVoiceVerifyCode(String mobile) {
        if (!RegsUtil.validatePhone(mobile)) {
            throw new ResponsiveException("请输入正确的手机号");
        }

        String verifyCodeKey = Constants.CACHE_USER_VERIFY_CODE + mobile;
        if (cache.hasKey(verifyCodeKey))
            cache.del(verifyCodeKey);

        SMSFactory sms = new NeteaseSMS();
        String verifyCode = sms.getSMSProvider().getVerifyCode();
        try {
            int statusCode = sms.getSMSProvider().getVoiceCode(mobile, verifyCode);
            if (AbstractSMSProvider.SUCCESS_CODE == statusCode) {
                //将验证码存入redis
                cache.set(verifyCodeKey, verifyCode, AbstractSMSProvider.ACTIVE_TIME);
            }

        } catch (Exception e) {
            throw new GeneralException("发送短信验证码失败", e);
        }
    }


    @Override
    public User getCurrentUser(ServletRequest request, ServletResponse response) {
        String token = JwtUtil.getToken(request);
        String uId = JwtUtil.getAccount(token);
        String cacheKey = Constants.CACHE_VISITOR_USER_INFO + uId;
        User user;
        if (cache.hasKey(cacheKey)) {
            user = (User) cache.get(cacheKey);
        } else {
            user = this.getOne(new QueryWrapper<User>().eq("id", uId));
            if (user == null)
                return null;
            cache.set(cacheKey, user, DateUtil.getSurplusSecondOfToday());
        }
        return user;
    }


    @Override
    public UserInfoVO getUserInfo() {
        User currentUser = UserContext.getCurrentUser();
        return getUserInfo(currentUser);
    }

    private UserInfoVO getUserInfo(User user) {
        if (user == null)
            throw new ResponsiveException(ExCodeMsgEnum.ACCOUNT_NOT_EXISTS);

        return UserInfoVO.builder().id(user.getId()).gender(user.getGender())
                .nickname(user.getNickname()).profile(user.getProfile()).userIcon(user.getUserIcon())
                .userLevel(user.getUserLevel()).phone(RegsUtil.shadowPhone(user.getPhone())).build();
    }


    @Override
    public BaseListVO<UserVO> findAll(UserDTO userDTO) {

        IPage<User> page = this.page(new Page<User>().setSize(userDTO.getPageSize()).setCurrent(userDTO.getPage()),
                new QueryWrapper<User>().orderByDesc("register_time"));

        List<User> list = page.getRecords();
        List<UserVO> voList = new ArrayList<>();
        if (!ListUtils.isEmpty(list)) {
            list.forEach(user -> {
                UserVO vo = new UserVO();
                BeanUtils.copyProperties(user, vo);
                voList.add(vo);
            });
        }

        return new BaseListVO<UserVO>().setList(voList).setTotal(page.getTotal());
    }


    @Override
    public UserVO findById(String id) {
        User user = this.getById(id);
        if (user == null)
            throw new ResponsiveException("用户不存在或已被删除");

        return UserVO.builder().id(user.getId()).vipType(user.getVipType()).macAddr(user.getMacAddr()).phone(user.getPhone())
                .registerTime(user.getRegisterTime()).vipEndTime(user.getVipEndTime())
                .userLevel(user.getUserLevel()).build();
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void remove(String id) {
        User user = this.getById(id);
        if (user == null)
            throw new ResponsiveException("用户不存在或已被删除");

        boolean removeSuccess = this.removeById(id);
        if (removeSuccess) {
            userSaveVideoRecordService.removeByUserId(id);
            userViewingRecordService.removeByUId(id);
            userVideoChannelService.removeByUId(id);
            userVideoCollectionService.removeByVidOrUId(UserVideoCollection.builder().userId(id).build());

            String cacheKey = Constants.CACHE_VISITOR_USER_INFO + user.getId();
            if (cache.hasKey(cacheKey)) {
                cache.del(cacheKey);
            }
        }

    }

    @Override
    public boolean updateUserById(User user) {
        User user2 = this.getById(user.getId());
        if (user2 == null)
            throw new ResponsiveException("用户不存在或已被删除");

        return doUpdateUser(user);
    }

    @Override
    public boolean doUpdateUser(User user) {
        boolean updated = this.updateById(user);
        if (updated) {
            String cacheKey = Constants.CACHE_VISITOR_USER_INFO + user.getId();
            User user2 = this.getById(user.getId());
            if (user2 != null) {
                cache.set(cacheKey, user2, DateUtil.getSurplusSecondOfToday());
            }
        }
        return updated;
    }

    @Override
    public boolean updateUser(User user) {
        User user2 = UserContext.getCurrentUser();
        if (user2 == null)
            throw new ResponsiveException("用户不存在或已被删除");
        if(user.getNickname()!=null){
            if(this.getOne(new QueryWrapper<User>().notIn("id",user2.getId()).eq("nickname", user.getNickname()))!=null) {
                throw new ResponsiveException("该用户名已存在！");
            }
        }

        user.setId(user2.getId());
        return doUpdateUser(user);
    }


    @Override
    public void updateLevelByUId(String id, Integer level) {
        this.doUpdateUser(User.builder().id(id).userLevel(level).build());
    }


    @Override
    public Boolean uploadPortrait(String imgPath) {
        User currentUser = UserContext.getCurrentUser();

        //完成福利任务首次上传头像
        boolean upSuccess = true;
        if (StringUtils.isBlank(currentUser.getUserIcon())) {
            upSuccess = userTaskRecordService.toDoFristUpPortrait(currentUser.getId());
        }
        return upSuccess && this.doUpdateUser(User.builder().userIcon(imgPath).id(currentUser.getId()).build());

    }

    @Override
    public UserVO bindPhone(String phone, String verifyCode) {

        String verifyCodeKey = StringUtils.join(Constants.CACHE_USER_VERIFY_CODE, phone, ":", verifyCode);
        if (!cache.hasKey(verifyCodeKey) || !Objects.equals(cache.get(verifyCodeKey), verifyCode)) {
            throw new ResponsiveException("短信验证码错误！");
        }

        User currentUser = UserContext.getCurrentUser();
        if (StringUtils.isBlank(currentUser.getPhone())) {
            //首次绑定手机号
            userTaskRecordService.toDoFristBindPhone(currentUser.getId());
        }
        // vip到期时间
        Date time = new Date();
        Date tim2 = time;
        if (currentUser.getVipEndTime() != null) {
            time = currentUser.getVipEndTime();
        }
        /*// 计算某一月份的最大天数  
        Calendar cal = Calendar.getInstance();
        // Date转化为Calendar  
        cal.setTime(time);
        // 一月后的1天前 
        cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
        Date endTime = cal.getTime();*/
        int compare = time.compareTo(tim2);
        boolean updated = true;
        if (compare == -1 || compare == 0) {
            updated = this.doUpdateUser(User.builder().vipType(2).phone(phone).id(currentUser.getId()).build());
        } else {
            updated = this.doUpdateUser(User.builder().vipType(1).vipEndTime(time).phone(phone).id(currentUser.getId()).build());
        }
        if (updated) {
            cache.delMatch(StringUtils.join(Constants.CACHE_USER_VERIFY_CODE, phone, ":*"));
        }
        return this.findById(currentUser.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void spreadRegister(String spreadCode, String registerChannel) {
        User spreadUser = this.getById(spreadCode);

        spreadUser.setRegisterChannel(registerChannel);

        //填写当前玩家等推广人
        User currentUser = UserContext.getCurrentUser();
        if (spreadUser != null && StringUtils.isBlank(currentUser.getRecommenderId())) {
            spreadUser.setRecommendCount(spreadUser.getRecommendCount() + 1);

            //更新用户层级
            UserLevel levelBySpread = userLevelService.findLevelBySpread(spreadUser.getRecommendCount());
            if (levelBySpread != null && !Objects.equals(levelBySpread.getLevel(), spreadUser.getUserLevel())) {
                //用户当前层级
                UserLevel currentLevel = userLevelService.getOne(new QueryWrapper<UserLevel>().eq("level", spreadUser.getUserLevel()));
                spreadUser.setUserLevel(levelBySpread.getLevel());
                spreadUser.setViewingCount(spreadUser.getViewingCount() - currentLevel.getViewingCount() + levelBySpread.getViewingCount());
                spreadUser.setSaveCount(spreadUser.getSaveCount() - currentLevel.getSaveCount() + levelBySpread.getSaveCount());
            }

            // vip到期时间
            Date time = new Date();
            if (spreadUser.getVipEndTime() != null) {
                int compare = spreadUser.getVipEndTime().compareTo(time);
                if (compare == 1) {
                    time = spreadUser.getVipEndTime();
                }
            }
            // 计算某一月份的最大天数  
            Calendar cal = Calendar.getInstance();
            // Date转化为Calendar  
            cal.setTime(time);
            // 一月后的1天前 
            cal.add(java.util.Calendar.DAY_OF_MONTH, 30);
            Date endTime = cal.getTime();
            spreadUser.setVipEndTime(endTime);

            boolean updated = this.doUpdateUser(spreadUser);

            //推广后台系统推广人数+1
            ManagerEmployee managerEmployee = managerEmployeeService.getByMacAddr(spreadUser.getMacAddr());
            if (managerEmployee != null) {
                managerEmployee.setRecommendCount(managerEmployee.getRecommendCount() + 1);
                managerEmployeeService.updateByObj(managerEmployee);
            }

            if (updated) {
                //更新推广任务 和 领取奖励
                userTaskRecordService.doPromotionTasks(spreadUser);

                log.info("推广玩家注册，推广码：{}，新玩家：{}", spreadCode, currentUser.getId());
                this.doUpdateUser(User.builder().id(currentUser.getId()).recommenderId(spreadCode).build());
            }
        }
    }

    @Override
    public List<SpreadRecordVO> spreadRecordList() {
        User currentUser = UserContext.getCurrentUser();
        List<SpreadRecordVO> voList = new ArrayList<>();
        List<User> recommenderList = this.list(new QueryWrapper<User>().eq("recommender_id", currentUser.getId()));

        if (!ListUtils.isEmpty(recommenderList)) {
            recommenderList.forEach(recommender -> voList.add(SpreadRecordVO.builder()
                    .inviterNickname(recommender.getNickname()).registerTime(recommender.getRegisterTime().getTime()).build()));
        }

        return voList;
    }


    @Override
    public boolean mobileIsExists(String mobile) {
        if (!RegsUtil.validatePhone(mobile)) {
            throw new ResponsiveException("请输入正确的手机号");
        }

        int count = this.count(new QueryWrapper<User>().eq("phone", mobile));
        if (RegsUtil.valiIsNonnegative(count))
            return true;

        return false;
    }

    @Override
    public void EndTime() {
        User currentUser = UserContext.getCurrentUser();
        String id = currentUser.getId();
        User user = this.getById(id);

        Date date = new Date();
        user.setLastTime((date.getTime() - user.getLastLoginTime().getTime()) / 60000);
        this.updateUser(user);

    }

    /*    @Override
    public void resendVerifyCode(String mobile) {
        String verifyCodeKey = Constants.CACHE_USER_VERIFY_CODE + mobile;
        if (cache.hasKey(verifyCodeKey))
            cache.del(verifyCodeKey);

        sendVerifyCode(mobile);
   }*/
}
