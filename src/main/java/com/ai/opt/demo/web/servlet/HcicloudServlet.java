package com.ai.opt.demo.web.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        super.doGet(req, resp);
    }
}
