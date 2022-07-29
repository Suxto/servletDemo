package com.example.servletdemo.servlets;


import com.example.servletdemo.Utils.JDBC;
import com.example.servletdemo.User;
import com.example.servletdemo.Utils.tools;
import com.example.servletdemo.myspringmvc.ViewBaseServlet;
import org.junit.Test;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@WebServlet("/index")
public class IndexServletTest extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");

        HttpSession session = request.getSession();
        String keyword = request.getParameter("keyword");

        if (tools.isnEmpty(keyword)) session.setAttribute("keyword", keyword);
        else session.setAttribute("keyword", null);

        doGet(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        List<User> list;
        long pageNo = 1, tot;
        if (request.getParameter("pageNo") != null) pageNo = Integer.parseInt(request.getParameter("pageNo"));
        session.setAttribute("pageNo", pageNo);

        String keyword = (String) session.getAttribute("keyword"), sql;

        System.out.println(keyword);

        if (tools.isnEmpty(keyword)) {
            session.setAttribute("keyword", keyword);

            sql = "select count(*) from users where uName rlike ? ;";
            tot = JDBC.getCount(sql, keyword);

            sql = "select * from users where uName rlike ?  limit ? , 6";
            list = JDBC.getForList(User.class, sql, keyword, (pageNo - 1) * 6);
        } else {
            sql = "select count(*) from users";
            tot =JDBC.getCount(sql);

            sql = "select * from users limit ? , 6";
            list = JDBC.getForList(User.class, sql, (pageNo - 1) * 6);
        }

        tot = (tot + 5) / 6;
        session.setAttribute("totPg", tot);
        session.setAttribute("userList", list);
        super.processTemplate("index", request, response);
    }

    @Test
    public void test1() {
        String sql = "select count(*) from users where uName rlike ?", keyword = "1";
        long tot = JDBC.getCount(sql, keyword);
        System.out.println(tot);
    }
}
