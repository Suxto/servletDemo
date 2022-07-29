package com.servletdemo.servlets;

import com.servletdemo.bean.User;
import com.servletdemo.myspringmvc.ViewBaseServlet;
import com.servletdemo.utils.JDBC;
import com.servletdemo.utils.Tools;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/index")
public class UserServlet extends ViewBaseServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf-8");
        String operate = req.getParameter("operate");
        if (Tools.isEmpty(operate)) operate = "index";
        switch (operate) {
            case "index":
                index(req, resp);
                break;
            case "search":
                search(req, resp);
                break;
            case "add":
                add(req, resp);
                break;
            case "edit":
                edit(req, resp);
                break;
            case "remove":
                remove(req, resp);
                break;
        }
    }

    private void index(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        List<User> list;
        long pageNo = 1, tot;
        if (req.getParameter("pageNo") != null) pageNo = Integer.parseInt(req.getParameter("pageNo"));
        session.setAttribute("pageNo", pageNo);

        String keyword = (String) session.getAttribute("keyword"), sql;

//        System.out.println(keyword);

        if (Tools.isnEmpty(keyword)) {
            session.setAttribute("keyword", keyword);

            sql = "select count(*) from users where uName rlike ? ;";
            tot = JDBC.getCount(sql, keyword);

            sql = "select * from users where uName rlike ?  limit ? , 6";
            list = JDBC.getForList(User.class, sql, keyword, (pageNo - 1) * 6);
        } else {
            sql = "select count(*) from users";
            tot = JDBC.getCount(sql);

            sql = "select * from users limit ? , 6";
            list = JDBC.getForList(User.class, sql, (pageNo - 1) * 6);
        }
        tot = (tot + 5) / 6;
        session.setAttribute("totPg", tot);
        session.setAttribute("userList", list);
        super.processTemplate("index", req, resp);
    }

    private void search(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String keyword = req.getParameter("keyword");
        if (Tools.isnEmpty(keyword)) session.setAttribute("keyword", keyword);
        else session.setAttribute("keyword", null);
        index(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uName = req.getParameter("uName");
        if (uName != null) {
            String uAge = req.getParameter("uAge");
            String uTel = req.getParameter("uTel");
            String sql = "insert into users (uName,uAge,uTel) values (?,?,?)";
            JDBC.upDate(sql, uName, uAge, uTel);
            resp.sendRedirect("index");
        } else {
            super.processTemplate("add", req, resp);
        }
    }

    private void edit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user;
        String sql;
        if (Tools.isnEmpty(req.getParameter("uName"))) {
            user = (User) session.getAttribute("user");
            user.setuName(req.getParameter("uName"));
            user.setuAge(Integer.parseInt(req.getParameter("uAge")));
            user.setuTel(req.getParameter("uTel"));
            sql = "update users set uName = ?, uAge = ?, uTel = ? where id = ?";
            JDBC.upDate(sql, user.getuName(), user.getuAge(), user.getuTel(), user.getId());
            resp.sendRedirect("index");
        } else {
            String id = req.getParameter("id");
            sql = "select * from users where id = ?";
            user = JDBC.getOne(User.class, sql, id);
            session.setAttribute("user", user);
            session.removeAttribute("userList");
            super.processTemplate("edit", req, resp);
        }
    }

    private void remove(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        int id = user.getId();
        String sql = "delete from users where id = ?";
        JDBC.upDate(sql, id);
        resp.sendRedirect("index");
    }
}
