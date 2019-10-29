package com.k365.video_common.handler.sms;

import com.alibaba.fastjson.JSON;
import com.k365.video_common.exception.GeneralException;
import com.k365.video_common.util.RandomKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Gavin
 * @date 2019/7/24 11:53
 * @description：网易云信短信服务商
 */
@Slf4j
public class NeteaseProvider implements AbstractSMSProvider {

    /**
     * 发送验证码的请求路径URL
     */
    private static final String SERVER_URL = "https://api.netease.im/sms/sendcode.action";

    /**
     * 校验验证码的请求路径URL
     */
    private static final String SERVER_CHECK_URL = "https://api.netease.im/sms/verifycode.action";

    /**
     * 网易云信分配的账号 管理后台应用下申请的Appkey
     */
    private static final String APP_KEY = "594219c134105b065ac315c770f4ed3b";

    /**
     * 网易云信分配的密钥 管理后台应用下申请的appSecret
     */
    private static final String APP_SECRET = "8c65e74742e5";

    /**
     * 短信模板ID
     */
    private static final String DEFAULT_TEMPLATEID = "14807377";

    /**
     * 语音短信模板
     */
    private static final String VOICE_TEMPLATEID = "14793523";

    /**
     * 成功状态码
     */
    private static final int NETEASE_SUCCESS_CODE = 200;

    @Override
    public int sendCode(String mobile, String code) throws Exception {
        log.info("手机号为：{} 的用户使用【网易云信】获取短信验证码,短信模板id：{}",mobile,VOICE_TEMPLATEID);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("templateid", DEFAULT_TEMPLATEID));
        nvps.add(new BasicNameValuePair("mobile", mobile));
        nvps.add(new BasicNameValuePair("authCode", code));
        return new NeteaseProvider().doSend(nvps,SERVER_URL);
    }

    @Override
    public int getVoiceCode(String mobile, String code) throws Exception {
        log.info("手机号为：{} 的用户使用【网易云信】获取语音验证码,短信模板id：{}",mobile,VOICE_TEMPLATEID);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("templateid", VOICE_TEMPLATEID));
        nvps.add(new BasicNameValuePair("mobile", mobile));
        nvps.add(new BasicNameValuePair("authCode", code));
        return new NeteaseProvider().doSend(nvps,SERVER_URL);
    }

    @Override
    public int checkCode(String mobile, String code) throws Exception {
        //设置请求参数
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("mobile", mobile));
        nvps.add(new BasicNameValuePair("code", code));
        return new NeteaseProvider().doSend(nvps,SERVER_CHECK_URL);
    }


    private int doSend(List<NameValuePair> nvps,String url) throws Exception {
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String nonce = RandomKeyUtil.createSecretKey();

        // 生成校验码
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, nonce, curTime);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", CONTENT_TYPE);

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));

        try {
            CloseableHttpResponse response = httpclient.execute(httpPost);
            String code = null;
            if (response.getStatusLine() != null && NETEASE_SUCCESS_CODE == response.getStatusLine().getStatusCode()) {
                code = JSON.parseObject(EntityUtils.toString(response.getEntity(), "utf-8")).getString("code");
                if(Objects.equals(""+NETEASE_SUCCESS_CODE , code)) {
                    return SUCCESS_CODE;
                }
            }
            System.out.println(code);
            log.error("网易云信发送短信验证码失败，code={}",code);

        } finally {
            if (httpclient != null)
                httpclient.close();
        }
        return FAIL_CODE;
    }

}
