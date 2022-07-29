package com.example.servletdemo.servlets;

import com.example.servletdemo.JDBC.Utils;
import com.example.servletdemo.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/add")
public class AddServlet extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf-8");
        String uName = req.getParameter("uName");
        if (uName != null) {
            String uAge = req.getParameter("uAge");
            String uTel = req.getParameter("uTel");
            String sql = "insert into users (uName,uAge,uTel) values (?,?,?)";
            Utils.upDate(sql, uName, uAge, uTel);
        }
        resp.sendRedirect("index");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.processTemplate("add", req, resp);
    }
}
