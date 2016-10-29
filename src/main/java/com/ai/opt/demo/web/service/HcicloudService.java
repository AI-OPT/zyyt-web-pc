package com.ai.opt.demo.web.service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  com.ai.opt.demo.web.utils.MD5Util;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by liutong on 16/10/27.
 */
public class HcicloudService {
    public static File file = new File("tts.wav");
    private static String APPKEY = "ad5d5421";
    private static String DEVKEY = "bca4b0015b309b76301bb10efdf90561";
    private static String SENDURL = "http://test.api.hcicloud.com:8880/tts/SynthText";
    private static final String PROXY_URL = "http://172.17.0.1:8180/demo-web/hclouts?forNum=1";
    private static final Logger LOGGER = LoggerFactory.getLogger(HcicloudService.class);
    public static List<String> TIME_LIST = new ArrayList<>();
    public List<String> proxyTime = new ArrayList<>();

    /**
     * 语音合成
     * 
     * @param text 合成文本不能超过1024字节，utf-8
     * @return
     * @author mimw
     */
    public byte[] ttsSynth(String text,boolean toFile) {
        long startTime = System.currentTimeMillis();
        LOGGER.info("开始 ttsSynth,当前时间戳:{}",startTime);
        CloseableHttpClient client = HttpClients.createDefault();  
        HttpPost post = new HttpPost(SENDURL);
        CloseableHttpResponse response = null;

        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String dataStr = dateFormater.format(date);
        System.out.println(dataStr);
        
        post.setHeader("x-app-key", APPKEY);
        post.setHeader("x-sdk-version", "3.6");
        post.setHeader("x-request-date", dateFormater.format(date));
        post.setHeader("x-task-config", "capkey=tts.cloud.wangjing,audioformat=mp3_16");
        post.setHeader("x-session-key", MD5Util.MD5(dataStr + DEVKEY ));
        post.setHeader("x-udid", "101:1234567890"); 
        
        System.out.println(MD5Util.MD5(dataStr + DEVKEY ));
        
        StringEntity  param =new StringEntity(text,  "UTF-8");// 构造请求数据
        post.setEntity(param);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            long httpStart = System.currentTimeMillis();
            LOGGER.info("开始httpClient,当前时间戳:{}",httpStart);
            response = client.execute(post);
            long httpEnd = System.currentTimeMillis();
            LOGGER.info("结束httpClient,当前时间戳:{},用时:{}",httpEnd,(httpEnd-httpStart));
            HttpEntity entity = response.getEntity();
            InputStream ttsIn = entity.getContent();

            if (toFile){
                //写入文件
                intoFile(file,entity.getContent());
                LOGGER.info(response.getStatusLine().getStatusCode()+"");
            }else {
                //写入缓存
                int nRead = 0;
                byte[] data = new byte[8092];
                while ((nRead = ttsIn.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                LOGGER.info("entity inputStream len:{}", buffer.size());
            }

            //转换为字符串
//            resStr = EntityUtils.toString(entity, "UTF-8");
//            LOGGER.info(resStr);
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
        long endTime = System.currentTimeMillis();
        long tim = endTime - startTime;
        TIME_LIST.add(startTime+","+endTime+","+tim);
        LOGGER.info("结束 ttsSynth,当前时间戳:{},用时:{}",endTime,tim);
        return buffer.toByteArray();
    }

    public void proxyTts(){
        long startTime = System.currentTimeMillis();
        LOGGER.info("开始 proxyTts,当前时间戳:{}",startTime);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet post = new HttpGet(PROXY_URL+"&"+startTime);
        CloseableHttpResponse response = null;
        String resStr = "";

        try {
            long httpStart = System.currentTimeMillis();
            LOGGER.info("开始httpClient,当前时间戳:{}",httpStart);
            response = client.execute(post);
            long httpEnd = System.currentTimeMillis();
            LOGGER.info("结束httpClient,当前时间戳:{},用时:{}",httpEnd,(httpEnd-httpStart));
            HttpEntity entity = response.getEntity();
            SimpleDateFormat dateFormater = new SimpleDateFormat("HHmmss");
            Date date=new Date();
            String dataStr = dateFormater.format(date);
            //写入文件
            File file = new File(dataStr+"tts.wav");
            intoFile(file,entity.getContent());
//            System.out.println(response.getStatusLine().getStatusCode());
//            resStr = EntityUtils.toString(entity, "UTF-8");
//            LOGGER.info(resStr);
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
        long endTime = System.currentTimeMillis();
        long tim = endTime - startTime;
        proxyTime.add(startTime+","+endTime+","+tim);
        LOGGER.info("结束 proxyTts,当前时间戳:{},用时:{}",endTime,tim);
    }

    private void intoFile(File file,InputStream inputStream){
        try {
            if (!file.exists())
                file.createNewFile();
            LOGGER.info(file.getAbsolutePath());
          
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            InputStream input = inputStream;
            byte[] buffer = new byte[1024*1024];  
            int lens;  
            while ((lens = input.read(buffer)) > -1 ) {  
                baos.write(buffer, 0, lens);  
            }  
            baos.flush();  
            
            String resStr = new String(baos.toByteArray(), "utf-8");
            //获取xml内容
            int indx = resStr.indexOf("</ResponseInfo>")+15;
            LOGGER.info(resStr.substring(0,indx));
            
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            //丢掉xml内容
            is.skip(indx);
            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());

            byte[] b = new byte[1024*1024];
            int len = 0;
            while((len = is.read(b)) != -1)
            {
                fos.write(b,0,len);
            }
            is.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        HcicloudService cloudService = new HcicloudService();
//        cloudService.ttsSynth("今天天气很好");
    }
    
}
