package com.servletdemo.myspringmvc;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/test"}, initParams = {
        @WebInitParam(name = "key", value = "value"),
        @WebInitParam(name = "user", value = "Suxton")
})
public class TestServlet extends HttpServlet {
    @Override

    public void init() {
        ServletConfig servletConfig = getServletConfig();
        String initVal = servletConfig.getInitParameter("key");
        System.out.println("initParameter = " + initVal);

        ServletContext servletContext = getServletContext();
        String servletContextInitParameter = servletContext.getInitParameter("loc");
        System.out.println("ContexParameter = " + servletContextInitParameter);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        ServletContext servletContext = req.getServletContext();
        String initParameter = servletContext.getInitParameter("loc");
        System.out.println("requset : " + initParameter);
        HttpSession session = req.getSession();
        initParameter = session.getServletContext().getInitParameter("loc");
        System.out.println("session : " + initParameter);
    }
}
