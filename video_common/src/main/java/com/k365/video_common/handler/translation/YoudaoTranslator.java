package com.k365.video_common.handler.translation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.k365.video_common.response.HttpClientResult;
import com.k365.video_common.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.util.ListUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gavin
 * @date 2019/8/5 12:39
 * @description：
 */
public class YoudaoTranslator extends AbstractTranslator {

    private static final String URL = "http://fanyi.youdao.com/translate";

    @Override
    public String doTranslate(String from, String to, String text) throws Exception {
        String type = StringUtils.join(from.toUpperCase(),"2",to.toUpperCase());
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("doctype", "json");
        paramsMap.put("type", type);
        paramsMap.put("i", text);

        return getContent( HttpClientUtil.doGet(URL, null, paramsMap));

    }


    protected String getContent(HttpClientResult httpClientResult){
        StringBuilder result = new StringBuilder();

        if (hasContent(httpClientResult)) {
            Object errorCode = JSON.parseObject((String) httpClientResult.getContent(), Map.class).get("errorCode");
            if (errorCode != null && (Integer) errorCode == 0) {
                List<?> translateResults = ListUtils.toList(JSON.parseObject((String) httpClientResult.getContent(), Map.class).get("translateResult"));
                if (!ListUtils.isEmpty(translateResults)) {
                    translateResults.forEach(translateResult -> {
                        List<?> maps = ListUtils.toList(translateResult);
                        if (!ListUtils.isEmpty(maps)) {
                            maps.forEach(obj ->
                                    result.append(JSON.parseObject(JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue), HashMap.class).get("tgt"))
                            );
                        }
                    });

                    return result.toString();
                }
            }
        }

        return null;
    }

}
