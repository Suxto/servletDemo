package com.servletdemo.controllers;

import com.servletdemo.bean.User;
import com.servletdemo.utils.JDBC;
import com.servletdemo.utils.Tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;

//@WebServlet("/user.do")
public class UserController {

    private String index(HttpSession session, String pageNo, String keyword) {
        List<User> list;
        int tot;
        if (pageNo == null) pageNo = "1";
        session.setAttribute("pageNo", Integer.parseInt(pageNo));
        String sql;

//        System.out.println(keyword);

        if (Tools.isnEmpty(keyword)) {
            session.setAttribute("keyword", keyword);

            sql = "select count(*) from users where uName rlike ? ;";
            tot = (int) JDBC.getCount(sql, keyword);

            sql = "select * from users where uName rlike ?  limit ? , 6";
            list = JDBC.getForList(User.class, sql, keyword, (Integer.parseInt(pageNo) - 1) * 6);
        } else {
            sql = "select count(*) from users";
            tot = (int) JDBC.getCount(sql);

            sql = "select * from users limit ? , 6";
            list = JDBC.getForList(User.class, sql, (Integer.parseInt(pageNo) - 1) * 6);
        }
        tot = (tot + 5) / 6;
        session.setAttribute("totPg", tot);
        session.setAttribute("userList", list);
        return "user";
//        super.processTemplate("user", req, resp);
    }

    private String search(String keyword, HttpSession session) {
        if (Tools.isnEmpty(keyword)) session.setAttribute("keyword", keyword);
        else session.setAttribute("keyword", null);
        return index(session, "1", keyword);
    }

    private String add(String uName, String uAge, String uTel) {
        if (uName != null) {
            String sql = "insert into users (uName,uAge,uTel) values (?,?,?)";
            JDBC.upDate(sql, uName, uAge, uTel);
            return "redirect:user.do";
        } else {
            return "add";
        }
    }

    private String edit(HttpSession session, String id) {
        String sql = "select * from users where id = ?";
        User user =JDBC.getOne(User.class, sql, id);
        session.setAttribute("user", user);
        return "edit";
    }

    private String update(HttpSession session, String uName, String uAge, String uTel) {
        User user = (User) session.getAttribute("user");
        user.setuName(uName);
        user.setuAge(Integer.parseInt(uAge));
        user.setuTel(uTel);
        String sql = "update users set uName = ?, uAge = ?, uTel = ? where id = ?";
        JDBC.upDate(sql, user.getuName(), user.getuAge(), user.getuTel(), user.getId());
//            resp.sendRedirect("user.do");
        return "redirect:user.do";
    }

    private String remove(HttpSession session) {
        User user = (User) session.getAttribute("user");
        int id = user.getId();
        String sql = "delete from users where id = ?";
        JDBC.upDate(sql, id);
        session.removeAttribute("user");
        return "redirect:user.do";
    }
}
