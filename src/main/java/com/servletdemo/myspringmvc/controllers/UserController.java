package com.servletdemo.myspringmvc.controllers;

import com.servletdemo.user.bean.User;
import com.servletdemo.user.service.impl.UserServiceImpl;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.servletdemo.utils.Tools.isEmpty;
import static com.servletdemo.utils.Tools.isnEmpty;

public class UserController {

    UserServiceImpl userService = null;

    private String index(HttpSession session, String pageNo, String keyword) {
        if (pageNo == null) pageNo = "1";
        if (isEmpty(keyword)) keyword = (String) session.getAttribute("keyword");
        List<User> list = userService.getUserList(keyword, pageNo);
        int tot = userService.getPageCount(keyword);
        session.setAttribute("pageNo", Integer.parseInt(pageNo));
        session.setAttribute("totPg", tot);
        session.setAttribute("userList", list);
        return "user";
    }

    private String search(String keyword, HttpSession session) {
        if (isnEmpty(keyword)) session.setAttribute("keyword", keyword);
        else session.setAttribute("keyword", null);
        return index(session, "1", keyword);
    }

    private String add(String uName, String uAge, String uTel) {
        if (uName != null) {
            userService.addUser(uName, uAge, uTel);
            return "redirect:user.do";
        } else {
            return "add";
        }
    }

    private String edit(HttpSession session, String id) {
        User user = userService.getUserById(id);
        session.setAttribute("user", user);
        return "edit";
    }

    private String update(HttpSession session, String uName, String uAge, String uTel) {
        User user = (User) session.getAttribute("user");
        userService.updateUser(user, uName, uAge, uTel);
        return "redirect:user.do";
    }

    private String remove(HttpSession session) {
        User user = (User) session.getAttribute("user");
        userService.removeUser(user);
        session.removeAttribute("user");
        return "redirect:user.do";
    }
}
