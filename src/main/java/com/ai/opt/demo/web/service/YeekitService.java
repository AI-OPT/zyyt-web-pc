package com.ai.opt.demo.web.service;

import com.ai.opt.demo.web.exception.HttpStatusException;
import com.ai.opt.demo.web.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liutong on 16/10/27.
 */
public class YeekitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(YeekitService.class);
    private static final String SERVER_URL = "http://api.yeekit.com/dotranslate.php";
    private static final String APP_KID = "58105e00cabc3";
    private static final String APP_KEY = "53eeb0bb6c1b613ab361a4f8057b2bd9";
    public static List<String> TIME_LIST = new ArrayList<>();
    public List<String> proxyTime = new ArrayList<>();

    /**
     * 发送 GET 请求（HTTP），K-V形式
     * @return
     */
    public String proxyYee() {
        long startTime = System.currentTimeMillis();
        LOGGER.info("开始 doGet,当前时间戳:{}",startTime);
        String apiUrl = "http://172.17.0.1:8180/demo-web/yees?forNum=1";
        String result = null;
        HttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpPost = new HttpGet(apiUrl);
            long httpStart = System.currentTimeMillis();
            LOGGER.info("开始 httpClient,当前时间戳:{}",httpStart);
            HttpResponse response = httpclient.execute(httpPost);
            LOGGER.info("执行状态码 : " + response.getStatusLine().getStatusCode());
            long httpEnd = System.currentTimeMillis();
            LOGGER.info("结束 httpClient,当前时间戳:{},用时:{}",httpEnd,(httpEnd-httpStart));
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long tim = endTime - startTime;
        proxyTime.add(startTime+","+endTime+","+tim);
        LOGGER.info("结束 proxyTts,当前时间戳:{},用时:{}",endTime,tim);
        LOGGER.info("result str \r\n"+result);
        return result;
    }

    /**
     * 进行中译英翻译,
     * @param text 要翻译中文信息,UTF-8编码
     */
    public Map<String,String> dotranslateZhToEn(String text) {
        return getDotrans("zh","en",text);
    }
    /**
     * 进行中译英翻译,
     * @param text 要翻译中文信息,UTF-8编码
     */
    public Map<String,String> dotranslateEnToZh(String text) {
        return getDotrans("en","zh",text);
    }

    private Map<String,String> getDotrans(String from,String to,String text) {
        long startTime = System.currentTimeMillis();
        LOGGER.info("开始 getDotrans,当前时间戳:{}",startTime);
        Map<String,String> result = new HashMap<>();
        try {
            Map<String,Object> postParams = new HashMap<>();
            postParams.put("from",from);//源语言
            postParams.put("to",to);//目标语言
            postParams.put("app_kid",APP_KID);//授权APP ID
            postParams.put("app_key",APP_KEY);//授权APP KEY
            postParams.put("text", URLEncoder.encode(text,"UTF-8"));//待翻译文本,UTF-8编码
            String resultStr = HttpUtil.doPost(SERVER_URL,postParams);
            JSONArray translateds = JSON.parseObject(resultStr)
                    .getJSONArray("translation")
                    .getJSONObject(0).getJSONArray("translated");
            StringBuffer sb = new StringBuffer();
            for (int i=0;i<translateds.size();i++) {
                JSONObject jsonObject = translateds.getJSONObject(i);
                sb.append(jsonObject.getString("text"));
            }
            result.put("text",sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long tim = endTime - startTime;
        TIME_LIST.add(startTime+","+endTime+","+tim);
        LOGGER.info("结束 getDotrans,当前时间戳:{},用时:{}",endTime,tim);
        return result;
    }
}
