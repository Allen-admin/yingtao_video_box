package com.k365.video_common.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.k365.video_common.response.HttpClientResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gavin
 * @date 2019/6/20 15:53
 * @description：
 */
@Slf4j
public class IPUtil {
    /**
     * @param request
     * @return
     */
    public static String getInnerIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "0";
        }
        String ip = request.getRemoteAddr();
        /**
         * ip是否合法
         */
        if (!RegsUtil.validateIp(ip)) {
            ip = "0";
        }
        return ip;
    }

    /**
     * 获取IP数值
     */
    public static long getIpNumber(HttpServletRequest request) {
        return getIpNumber(getClientIp(request));
    }

    /**
     * 获取IP数值
     */
    public static long getIpNumber(String ipStr) {
        Long ipNumber = 0L;
        if (StringUtils.isNotBlank(ipStr)) {
            log.info("客户端IP转为数值型:【{}】",ipStr);
            ipNumber = Long.valueOf(ipStr.replaceAll("[^0-9]", ""));
        }
        return ipNumber;
    }

    /**
     * 获取客户端外网IP
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "0";
        }

        String ip = request.getHeader("X-Real-IP");
        if (ip == null || "".equals(ip.trim()) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0.0.0.0".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "127.0.0.1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * ping 域名
     *
     * @param ipAddress 域名
     * @param timeOut   超时时间 ms
     * @return
     * @throws Exception
     */
    public static Map<String, Object> pingDomain(String ipAddress, int timeOut) throws Exception {
        //超时时间 单位：毫秒
        timeOut = timeOut < 3000 ? 3000 : (timeOut > 30000 ? 30000 : timeOut);
        long begin = System.currentTimeMillis();
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        long time = System.currentTimeMillis() - begin;
        Map<String, Object> result = new HashMap<>();
        // 当返回值是true时，说明host是可用的，false则不可。
        result.put("state", status);
        result.put("time_ms", time);

        return result;
    }

    /**
     * 获取IP所在地
     *
     * @return
     * @throws Exception
     */
    public static String getLocationOfIP(String ipAddress) throws Exception {
        String url = "http://ip.taobao.com/service/getIpInfo2.php";
        Map<String, String> params = new HashMap<>();
        params.put("ip", ipAddress);
        HttpClientResult httpClientResult = HttpClientUtil.doPost(url, params);
        if (httpClientResult.getCode() == 200) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSON(httpClientResult.getContent()).toString());
            Integer code = Integer.valueOf(jsonObject.get("code").toString());
            if (code == 0) {
                JSONObject data = JSONObject.parseObject(jsonObject.get("data").toString());
                return StringUtils.join(data.get("country"), "-", data.get("region"), "-", data.get("city"));
            }

        }

        return null;
    }

}
