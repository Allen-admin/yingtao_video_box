package com.k365.manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.common.UserContext;
import com.k365.manager_service.DomainService;
import com.k365.manager_service.SysConfParamService;
import com.k365.user_service.UserService;
import com.k365.video_base.common.DomainTypeEnum;
import com.k365.video_base.mapper.DomainMapper;
import com.k365.video_base.model.dto.DomainDTO;
import com.k365.video_base.model.po.Domain;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.DomainVO;
import com.k365.video_common.constant.AppTypeEnum;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_common.constant.SysParamValueNameEnum;
import com.k365.video_common.exception.GeneralException;
import com.k365.video_common.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import javax.servlet.ServletRequest;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
public class DomainServiceImpl extends ServiceImpl<DomainMapper, Domain> implements DomainService {

    @Autowired
    private SysConfParamService sysConfParamService;

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private DomainService domainService;

    @Override
    public List<DomainVO> findUsable(AppTypeEnum appType) {
        DomainTypeEnum domainTypeEnum = Objects.equals(DomainTypeEnum.HEIMEI_DOMAIN.key(),appType.getKey()) ?
                DomainTypeEnum.HEIMEI_DOMAIN : DomainTypeEnum.XIAOAI_DOMAIN;

        List<Domain> list = this.list(new QueryWrapper<Domain>()
                .eq("status", Boolean.TRUE).eq("domain_type",domainTypeEnum.key()).orderByAsc("sort"));

        List<DomainVO> voList = new ArrayList<>();
        if (!ListUtils.isEmpty(list)) {
            list.forEach(domain -> voList.add(DomainVO.builder().domain(domain.getDomain()).build()));
        }
        return voList;
    }

    @Override
    public void add(DomainDTO domainDTO) {
        Domain domain = new Domain();
        BeanUtils.copyProperties(domainDTO, domain);
        domain.setUpdateTime(new Date());
        this.save(domain);
    }

    @Override
    public void update(DomainDTO domainDTO) {
        Domain domain = new Domain();
        BeanUtils.copyProperties(domainDTO, domain);
        domain.setId(domainDTO.getId());
        domain.setUpdateTime(new Date());

        this.updateById(domain);
    }

    @Override
    public BaseListVO<Domain> getByPage(DomainDTO domainDTO,ServletRequest request) {
        IPage page = this.page(new Page<Domain>().setCurrent(domainDTO.getPage()).setSize(domainDTO.getPageSize()),
                new QueryWrapper<Domain>().orderByAsc("status").orderByDesc("update_time"));

        if (page == null || page.getRecords() == null)
            return new BaseListVO<>();

        long total = page.getTotal();
        return new BaseListVO<Domain>().setList(page.getRecords()).setTotal(total);
    }

    @Override
    public void remove(Integer id) {
        this.removeById(id);
    }

    @Override
    public Map<String, Object> getSpreadDomain() {
        User currentUser = UserContext.getCurrentUser();

        SysParamValueNameEnum valName = SysParamValueNameEnum.XIAOAI_PAGE_URl;
        DomainTypeEnum domainType = DomainTypeEnum.XIAOAI_OFFICIAL_DOMAIN;

        if(Objects.equals(AppTypeEnum.HEI_MEI.getKey(),currentUser.getAppType())){
            valName = SysParamValueNameEnum.HEIMEI_PAGE_URl;
            domainType = DomainTypeEnum.HEIMEI_OFFICIAL_DOMAIN;
        }

        Map<String, Object> resultMap = new HashMap<>();
        String downloadAppUrl = (String) sysConfParamService.getValByValName(valName);
        if (StringUtils.isNotBlank(downloadAppUrl)) {
            downloadAppUrl = StringUtils.join(downloadAppUrl, "?code=", currentUser.getId());
        }

        List<Domain> list = this.list(new QueryWrapper<Domain>().eq("status", StatusEnum.ENABLE.key())
                .eq("domain_type", domainType.key()).orderByAsc("sort"));

        String domain = null;
        if (!ListUtils.isEmpty(list)) {
            domain = list.get(0).getDomain();
        }

        AppTypeEnum appType = AppTypeEnum.getByKey(currentUser.getAppType());
        String spreadQrcodeUrl = currentUser.getSpreadQrcodeUrl();

        if(StringUtils.isBlank(spreadQrcodeUrl) || !userService.verifySpreadQr(spreadQrcodeUrl,appType)){
            try {
                spreadQrcodeUrl = userService.createSpreadQrcodeImg(currentUser.getId(),spreadQrcodeUrl,appType);
            } catch (Exception e) {
                throw new GeneralException("生成推广二维码失败！",e);
            }
        }

        resultMap.put("domain", domain);
        resultMap.put("spreadUrl", downloadAppUrl);
        resultMap.put("img", spreadQrcodeUrl);
        return resultMap;
    }


    @Override
    public String getDomain(ServletRequest request){
        //获取请求头判断是浏览器还是app  返回不同的域名
        OSEnum os3 = HttpUtil.getOS3(request);
        Integer type=null;
        if(os3.getName().equals("Browser")){
            type=6;
        }else {
            type=5;
        }

        //获取域名
        DomainTypeEnum domainTypeEnum =null;
        if(type==5){
            domainTypeEnum=DomainTypeEnum.FILM_PICTURE_DOMAIN_APP; //APP
        }
        if(type==6){
            domainTypeEnum=DomainTypeEnum.FILM_PICTURE_DOMAIN_BROWSER;//浏览器
        }
        int key = domainTypeEnum.key();
        Domain domain = domainService.getOne(new QueryWrapper<Domain>().eq("domain_type",key));

        return domain.getDomain();
    }

    @Override
    public String getAppPicDomain(){

        //获取域名
        DomainTypeEnum domainTypeEnum =DomainTypeEnum.FILM_APPPICTURE_DOMAIN_APP;

        int key = domainTypeEnum.key();
        Domain domain = domainService.getOne(new QueryWrapper<Domain>().eq("domain_type",key));

        return domain.getDomain();
    }

}
