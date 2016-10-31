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
        //nginx代理模式
        String nProxyg = req.getParameter("nproxy");
        String dotransStr = "The Hyper-Text Transfer Protocol (HTTP) is perhaps the most significant protocol " +
                "used on the Internet today. Web services, network-enabled appliances and the growth of network " +
                "computing continue to expand the role of the HTTP protocol beyond user-driven web browsers, " +
                "while increasing the number of applications that require HTTP support." +
                "Although the java.net package provides basic functionality for accessing resources via HTTP, " +
                "it doesn't provide the full flexibility or functionality needed by many applications." +
                " The Jakarta Commons HttpClient component seeks to fill this void by providing an efficient," +
                " up-to-date, and feature-rich package implementing the client side of the most recent HTTP standards" +
                " and recommendations. See the Features page for more details on standards compliance and capabilities." +
                "Designed for extension while providing robust support for the base HTTP protocol, " +
                "the HttpClient component may be of interest to anyone building HTTP-aware client applications such as web ";
        int forNum = forNumStr == null ? 1 : Integer.parseInt(forNumStr);
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-type","text/html;charset=UTF-8");
        LOGGER.info("The params:forNum={},proxy={},toFile={},nproxy{}",forNum,proxy,toFile,nProxyg);
        //代理
        if (proxy!=null) {
            proxy(forNum);
        }//文本
        else if(toFile!=null){
            LOGGER.info("开始[本地] 执行翻译,当前时间:{}", startTime);
            YeekitService yeekitService = new YeekitService();
            yeekitService.TIME_LIST.clear();
            for (int i=0;i<forNum;i++) {
                yeekitService.dotranslateEnToZh(dotransStr+System.currentTimeMillis(),nProxyg!=null);
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info("结束[本地] 执行翻译,当前时间:{},用时:{}", endTime, (endTime - startTime));
            LOGGER.info(JSON.toJSONString(yeekitService.TIME_LIST,true));
        }//直接模式
        else{
            LOGGER.info("开始 执行翻译,当前时间:{}", startTime);
            YeekitService yeekitService = new YeekitService();
            Map<String,String> retMap =yeekitService.dotranslateEnToZh(dotransStr+System.currentTimeMillis(),false);
            resp.getWriter().write(retMap.get("text"));
            long endTime = System.currentTimeMillis();
            LOGGER.info("结束 执行翻译,当前时间:{},用时:{}", endTime, (endTime - startTime));
        }
        LOGGER.info("The params:forNum={},proxy={},toFile={},nproxy{}",forNum,proxy,toFile,nProxyg);
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
