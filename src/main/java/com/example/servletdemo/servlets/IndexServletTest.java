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
import java.util.List;


@WebServlet("/index")
public class IndexServletTest extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        long pageNo ;
        if (request.getParameter("pageNo") == null) pageNo = 1;
        else pageNo = Integer.parseInt(request.getParameter("pageNo"));
        session.setAttribute("pageNo", pageNo);

        String sql = "select count(*) from users";

        long tot = Utils.getCount(sql) == null ? 1 : Utils.getCount(sql);
        tot = tot / 6 + (tot % 6 > 0 ? 1 : 0);
        session.setAttribute("totPg", tot);

        sql = "select * from users limit ? , 6";
        List<User> list = Utils.getForList(User.class, sql, (pageNo - 1) * 6);

        session.setAttribute("userList", list);
        super.processTemplate("index", request, response);
    }

    @Test
    public void test1() {
        String sql = "select count(*) from users";
        Long tot = Utils.getCount(sql);
        System.out.println(tot);
        sql = "select * from users limit ? , 6";
        List<User> list = Utils.getForList(User.class, sql, 0);
        assert list != null;
        list.forEach(System.out::println);
    }
}
