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
public class DemoServletTest extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String fname = request.getParameter("fname");
        int price = Integer.parseInt(request.getParameter("price"));
        //...
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sql = "select * from users";
        List<User> list = Utils.getForList(User.class, sql);

        HttpSession session = request.getSession();
        session.setAttribute("userList", list);
        super.processTemplate("index", request, response);
    }

    @Test
    public void test1() {
        String sql = "select * from users";
        List<User> list = Utils.getForList(User.class, sql);
        System.out.println();
        assert list != null;
        list.forEach(System.out::println);
    }
}
