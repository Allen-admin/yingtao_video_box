package com.k365.video_common.handler.translation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.k365.video_common.response.HttpClientResult;
import com.k365.video_common.util.HttpClientUtil;
import org.thymeleaf.util.ListUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gavin
 * @date 2019/8/5 12:40
 * @description：
 */
public class GoogleTranslator extends AbstractTranslator {

    private static final String URL = "http://translate.google.cn/translate_a/single";

    @Override
    public String doTranslate(String from, String to, String text) throws Exception{
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("client", "gtx");
        paramsMap.put("dt", "t");
        paramsMap.put("dj", "1");
        paramsMap.put("ie", ENCODING);
        paramsMap.put("sl", from);
        paramsMap.put("tl", to);
        paramsMap.put("q", text);

        return getContent(HttpClientUtil.doGet(URL, null, paramsMap));

    }

    protected String getContent(HttpClientResult httpClientResult){
        StringBuilder result = new StringBuilder();
        if (hasContent(httpClientResult)) {

            List<?> sentences = ListUtils.toList(JSON.parseObject((String) httpClientResult.getContent(), Map.class).get("sentences"));


            sentences.forEach(sentence -> {
                if (sentence != null)
                    result.append(JSON.parseObject(JSON.toJSONString(sentence, SerializerFeature.WriteMapNullValue), HashMap.class).get("trans"));
            });

            return result.toString();
        }

        return null;
    }

}
