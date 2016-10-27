package com.ai.opt.demo.web.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  com.ai.opt.demo.web.utils.MD5Util;

/**
 * Created by liutong on 16/10/27.
 */
public class HcicloudService {
    private static String APPKEY = "ad5d5421";
    private static String DEVKEY = "bca4b0015b309b76301bb10efdf90561";
    private static String SENDURL = "http://test.api.hcicloud.com:8880/tts/SynthText";
    private static final Logger LOGGER = LoggerFactory.getLogger(HcicloudService.class);
    
    /**
     * 语音合成
     * 
     * @param text 合成文本不能超过1024字节，utf-8
     * @return
     * @author mimw
     */
    public String ttsSynth(String text) {
        CloseableHttpClient client = HttpClients.createDefault();  
        HttpPost post = new HttpPost(SENDURL);
        CloseableHttpResponse response = null;
        String resStr = "";
        
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String dataStr = dateFormater.format(date);
        System.out.println(dataStr);
        
        post.setHeader("x-app-key", APPKEY);
        post.setHeader("x-sdk-version", "3.6");
        post.setHeader("x-request-date", dateFormater.format(date));
        post.setHeader("x-task-config", "capkey=tts.cloud.wangjing");
        post.setHeader("x-session-key", MD5Util.MD5(dataStr + DEVKEY ));
        post.setHeader("x-udid", "101:1234567890"); 
        
        System.out.println(MD5Util.MD5(dataStr + DEVKEY ));
        
        StringEntity  param =new StringEntity(text,  "UTF-8");// 构造请求数据
        post.setEntity(param);
        try {
            response = client.execute(post);
            HttpEntity entity = response.getEntity();
            System.out.println(response.getStatusLine().getStatusCode());
            resStr = EntityUtils.toString(entity, "UTF-8");
            LOGGER.info(resStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resStr;
    }
    
}
