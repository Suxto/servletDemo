package com.servletdemo.myspringmvc.servlets;

import com.servletdemo.myspringmvc.io.BeanFactory;
import com.servletdemo.myspringmvc.io.ClassPathXmlApplicationContext;
import com.servletdemo.utils.Tools;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {
    private BeanFactory beanFactory;

    public void init() {
        super.init();
        beanFactory = new ClassPathXmlApplicationContext();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
//        req.setCharacterEncoding("utf-8");
        String servletPath = req.getServletPath();
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex);


        Object controllerObj = beanFactory.getBean(servletPath);


        String operate = req.getParameter("operate");
        if (Tools.isEmpty(operate)) operate = "index";

        try {
            Method[] methods = controllerObj.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (operate.equals(method.getName())) {
                    Parameter[] parameters = method.getParameters();
                    Object[] parameterValues = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        String parameterName = parameter.getName();
//                        System.out.println(parameterName);
                        if ("session".equals(parameterName)) {
                            parameterValues[i] = req.getSession();
                        } else {

                            String parameterValue = req.getParameter(parameterName);
                            parameterValues[i] = parameterValue;
                        }
                    }
                    method.setAccessible(true);
                    Object returnObj = method.invoke(controllerObj, parameterValues);

                    String returnStr = (String) returnObj;
                    if (returnStr.startsWith("redirect:")) {
                        String newDirection = returnStr.substring("redirect:".length());
                        resp.sendRedirect(newDirection);

                    } else {
                        super.processTemplate(returnStr, req, resp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
