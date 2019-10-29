package com.k365.video_base.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.k365.video_common.constant.ExCodeMsgEnum;
import com.k365.video_common.constant.HttpStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author Gavin
 * @date 2019/6/19 15:22
 * @description：响应结果生成工厂类
 */
@Slf4j
public class ResultFactory {

    public static String buildSuccessResult() {
        return buidResult(HttpStatusEnum.SUCCESS.value(), HttpStatusEnum.SUCCESS.getReasonPhrase(), null);
    }

    public static String buildSuccessResult(Object data) {
        return buidResult(HttpStatusEnum.SUCCESS.value(), HttpStatusEnum.SUCCESS.getReasonPhrase(), data);
    }

    public static String buildNotNullResult(Object data) {
        HttpStatusEnum statusEnum = data == null ? HttpStatusEnum.NOT_DATA :
                (!(data instanceof Collection) ? HttpStatusEnum.SUCCESS :
                        (CollectionUtils.isEmpty((Collection<?>) data) ? HttpStatusEnum.NOT_DATA : HttpStatusEnum.SUCCESS));
        return buidResult(statusEnum.value(), statusEnum.getReasonPhrase(), data);
    }

    public static String buildFailResult(String message) {
        return buidResult(HttpStatusEnum.HANDLE_REQUEST_FAIL.value(), message, null);
    }

    public static String buidResult(ExCodeMsgEnum resultCode, String message, Object data) {
        return buidResult(resultCode.code(), message, data);
    }

    public static String buidResult(int code, String message) {
        return buidResult(code, message, null);
    }

    public static String buidResult(int resultCode, String message, Object data) {
        String resultJsonStr = null;
        try {
            Result result = new Result(resultCode, message, data);
            resultJsonStr = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteEnumUsingToString);
        } catch (Exception e) {
            log.error("返回数据转JSON字符串失败，e = 【{}】",e);
        } finally {
            if(UserContext.getCurrentUser() != null){
                UserContext.context.remove();
            }

            if(SysUserContext.getCurrentSysUser() != null){
                SysUserContext.current.remove();
            }
        }
        return resultJsonStr;
    }
}
