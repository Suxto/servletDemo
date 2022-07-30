package com.servletdemo.utils;

import com.servletdemo.bean.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

//@WebServlet("/user.do")
public class UserControllerWithRequest {

    private String index(HttpServletRequest req) {
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
        return "user";
//        super.processTemplate("user", req, resp);
    }

    private String search(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String keyword = req.getParameter("keyword");
        if (Tools.isnEmpty(keyword)) session.setAttribute("keyword", keyword);
        else session.setAttribute("keyword", null);
        return index(req);
    }

    private String add(HttpServletRequest req) {
        String uName = req.getParameter("uName");
        if (uName != null) {
            String uAge = req.getParameter("uAge");
            String uTel = req.getParameter("uTel");
            String sql = "insert into users (uName,uAge,uTel) values (?,?,?)";
            JDBC.upDate(sql, uName, uAge, uTel);
//            resp.sendRedirect("user.do");
            return "redirect:user.do";
        } else {
//            super.processTemplate("add", req, resp);
            return "add";
        }
    }

    private String edit(HttpServletRequest req) {
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
//            resp.sendRedirect("user.do");
            return "redirect:user.do";
        } else {
            String id = req.getParameter("id");
            sql = "select * from users where id = ?";
            user = JDBC.getOne(User.class, sql, id);
            session.setAttribute("user", user);
            session.removeAttribute("userList");
//            super.processTemplate("edit", req, resp);
            return "edit";
        }
    }

    private String remove(HttpServletRequest req) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        int id = user.getId();
        String sql = "delete from users where id = ?";
        JDBC.upDate(sql, id);
//        resp.sendRedirect("user.do");
        return "redirect:user.do";
    }
}
