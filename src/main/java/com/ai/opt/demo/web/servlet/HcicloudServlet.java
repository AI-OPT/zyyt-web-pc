package com.ai.opt.demo.web.servlet;

import com.ai.opt.demo.web.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.opt.demo.web.service.HcicloudService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 灵云demo
 * Created by liutong on 16/10/26.
 */
public class HcicloudServlet extends HttpServlet{
    private static final Logger LOGGER = LoggerFactory.getLogger(HcicloudServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String forNumStr = req.getParameter("forNum");
        //是否使用代理
        String proxy = req.getParameter("proxy");
        int forNum = forNumStr == null ? 1 : Integer.parseInt(forNumStr);
        if (proxy==null) {
            LOGGER.info("开始执行语音合成,当前时间:{}", startTime);
            HcicloudService hcicloudService = new HcicloudService();
            
            for (int i = 0; i < forNum; i++) {
                hcicloudService.ttsSynth("今天天气很好");
            }
            
            long endTime = System.currentTimeMillis();
            LOGGER.info("结束执行语音合成,当前时间:{},用时:{}", endTime, (endTime - startTime));
        }else
            proxy(forNum);
    }
    
    /**
     * 代理模式
     */
    private void proxy(int forNum){
        long startTime = System.currentTimeMillis();
        LOGGER.info("HcicloudServlet.代理开始执行翻译,当前时间:{}", startTime);
        for (int i = 0; i < forNum; i++) {
            HttpUtil.doGet("http://123.56.4.39:8180/demo-web/yees?forNum=1");
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("HcicloudServlet.代理结束执行翻译,当前时间:{},用时:{}", endTime, (endTime - startTime));
    }
}
