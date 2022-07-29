package com.servletdemo.myspringmvc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf-8");
        String servletPath = req.getServletPath();
        System.out.println(servletPath);
        super.processTemplate("user", req, resp);
    }
}
