package com.example.servletdemo.servlets;

import com.example.servletdemo.JDBC.Utils;
import com.example.servletdemo.User;
import com.example.servletdemo.myspringmvc.ViewBaseServlet;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/edit.do")
public class EditServletTest extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String toRemove = req.getParameter("remove");
        HttpSession session = req.getSession();
        System.out.println(id+' '+toRemove);
        if (toRemove != null) {
            String sql = "delete from users where id = ?";
            Utils.upDate(sql, id);
            resp.sendRedirect("index");
        } else {
            String sql = "select * from users where id = ?";
            User user = Utils.getOne(User.class, sql, id);
//        System.out.println(user);
            session.setAttribute("user", user);
            super.processTemplate("edit", req, resp);
        }
    }

    @Test
    public void test() {
        String id = "2";
        String sql = "select * from users where id = ?";
        User user = Utils.getOne(User.class, sql, id);
        System.out.println(user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        int id = user.getId();
        user.setuName(req.getParameter("uName"));
        user.setuAge(Integer.parseInt(req.getParameter("uAge")));
        user.setuTel(req.getParameter("uTel"));
        String sql = "update users set uName = ?, uAge = ?, uTel = ? where id = ?";
        Utils.upDate(sql, user.getuName(), user.getuAge(), user.getuTel(), user.getId());
        resp.sendRedirect("index");
    }
}
