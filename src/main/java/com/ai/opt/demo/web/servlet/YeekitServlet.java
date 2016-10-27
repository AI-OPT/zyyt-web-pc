package com.ai.opt.demo.web.servlet;

import com.ai.opt.demo.web.service.YeekitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liutong on 16/10/27.
 */
public class YeekitServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(YeekitServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("开始执行翻译,当前时间:{}",startTime);
        YeekitService yeekitService = new YeekitService();
        for (int i=0;i<10;i++){
            yeekitService.dotranslateEnToZh("Who am I?");
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("结束执行翻译,当前时间:{},用时:{}",endTime,(endTime-startTime));
    }
}
