package com.ai.opt.demo.web.servlet;

import com.ai.opt.demo.web.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.opt.demo.web.service.HcicloudService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 灵云demo
 * Created by liutong on 16/10/26.
 */
public class HcicloudServlet extends HttpServlet{
    private static final Logger LOGGER = LoggerFactory.getLogger(HcicloudServlet.class);
    private static List<String> timeList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String forNumStr = req.getParameter("forNum");
        //是否使用代理
        String proxy = req.getParameter("proxy");
        //是否写入文件
        String toFile = req.getParameter("toFile");
        int forNum = forNumStr == null ? 1 : Integer.parseInt(forNumStr);
        String text = "结束执行翻译,当前时间:1477561176545,用时:2706结束httpClient,当前时间戳:1477561176022," +
                "用时:495结束httpClient,当前时间戳:1477561176022,用时:495结束httpClient,当前时间戳:1477561176022," +
                "用时:495结束httpClient,当前时间戳:1477561176022,用时:495结束httpClient,当前时间戳:1477561176022," +
                "用时:495结束httpClient,当前时间戳:1477561176022,用时:495结束httpClient,当前时间戳:1477561176022," +
                "用时:495结束httpClient,当前时间戳:1477561176022,用时:495结束httpClient,当前时间戳:1477561176022," +
                "用时:495结束httpClient,当前时间戳:1477561176022,用时:495结束httpClient,当前时间戳:1477561176022," +
                "用时:495结束httpClient,当前时间戳:1477561176022,用时:495结束httpClient,当前时间戳:1477561176022," +
                "用时:495结束httpClient,当前时间戳,用时:495结束httpClient,";
        //代理模式
        if (proxy!=null){
            proxy(forNum);
        }//写入文件
        else if (toFile!=null){
            HcicloudService.TIME_LIST.clear();
            LOGGER.info("开始[文本] 执行语音合成,当前时间:{}", startTime);
            HcicloudService hcicloudService = new HcicloudService();
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Content-type","text/html;charset=UTF-8");
            for (int i=0;i<forNum;i++) {
                hcicloudService.ttsSynth(text + System.currentTimeMillis(), true);
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info("结束[文本] 执行语音合成,当前时间:{},用时:{}", endTime, (endTime - startTime));
            LOGGER.info(JSON.toJSONString(hcicloudService.TIME_LIST,true));
        }//直接返回模式
        else{
            LOGGER.info("开始 执行语音合成,当前时间:{}", startTime);
            HcicloudService hcicloudService = new HcicloudService();
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Content-type","text/html;charset=UTF-8");
            resp.getOutputStream();
            byte[] ttsBytes = hcicloudService.ttsSynth(text+startTime,false);
            resp.getOutputStream().write(ttsBytes);
            long endTime = System.currentTimeMillis();
            LOGGER.info("结束 执行语音合成,当前时间:{},用时:{}", endTime, (endTime - startTime));
            LOGGER.info(JSON.toJSONString(hcicloudService.TIME_LIST,true));
        }
    }
    
    /**
     * 代理模式
     */
    private void proxy(int forNum){
        long startTime = System.currentTimeMillis();
        LOGGER.info("开始 HcicloudServlet.代理开始执行翻译,当前时间:{}", startTime);
        HcicloudService hcicloudService = new HcicloudService();
        hcicloudService.proxyTime.clear();
        for (int i = 0; i < forNum; i++) {
            hcicloudService.proxyTts();
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("结束 HcicloudServlet.代理结束执行翻译,当前时间:{},用时:{}", endTime, (endTime - startTime));
        LOGGER.info(JSON.toJSONString(hcicloudService.proxyTime,true));
    }
}
