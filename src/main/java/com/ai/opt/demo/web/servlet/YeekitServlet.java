package com.ai.opt.demo.web.servlet;

import com.ai.opt.demo.web.service.YeekitService;
import com.ai.opt.demo.web.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by liutong on 16/10/27.
 */
public class YeekitServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(YeekitServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String forNumStr = req.getParameter("forNum");
        //是否使用代理
        String proxy = req.getParameter("proxy");
        //是否写入文件
        String toFile = req.getParameter("toFile");
        int forNum = forNumStr == null ? 1 : Integer.parseInt(forNumStr);
        //代理
        if (proxy!=null) {
            proxy(forNum);
        }//文本
        else if(toFile!=null){
            LOGGER.info("开始[本地] 执行翻译,当前时间:{}", startTime);
            YeekitService yeekitService = new YeekitService();
            yeekitService.TIME_LIST.clear();
            for (int i=0;i<forNum;i++) {
                yeekitService.dotranslateEnToZh("Who am I?"+System.currentTimeMillis());
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info("结束[本地] 执行翻译,当前时间:{},用时:{}", endTime, (endTime - startTime));
            LOGGER.info(JSON.toJSONString(yeekitService.TIME_LIST,true));
        }//直接模式
        else{
            LOGGER.info("开始 执行翻译,当前时间:{}", startTime);
            YeekitService yeekitService = new YeekitService();
            Map<String,String> retMap =yeekitService.dotranslateEnToZh("Who am I?");
            resp.getWriter().write(retMap.get("text"));
            long endTime = System.currentTimeMillis();
            LOGGER.info("结束 执行翻译,当前时间:{},用时:{}", endTime, (endTime - startTime));
        }
    }

    /**
     * 代理模式
     */
    private void proxy(int forNum){
        long startTime = System.currentTimeMillis();
        LOGGER.info("代理开始执行翻译,当前时间:{}", startTime);
        YeekitService yeekitService = new YeekitService();
        for (int i = 0; i < forNum; i++) {
            yeekitService.proxyYee();
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("代理结束执行翻译,当前时间:{},用时:{}", endTime, (endTime - startTime));
        LOGGER.info(JSON.toJSONString(yeekitService.proxyTime,true));
    }
}
