package com.servletdemo.myspringmvc;

import com.servletdemo.utils.Tools;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {

    private final Map<String, Object> beanMap = new HashMap<>();

    public void init() {
        super.init();
        InputStream inputStream = DispatcherServlet.class.getResourceAsStream("/applicationContext.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList beanNodeList = document.getElementsByTagName("bean");

            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    Constructor<?> constructor = Class.forName(className).getConstructor();
                    Object beanObj = constructor.newInstance();
//                    Object beanObj = Class.forName(className).newInstance();
                    beanMap.put(beanId, beanObj);

//                    System.out.println(beanId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf-8");
        String servletPath = req.getServletPath();
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex);


        Object controllerObj = beanMap.get(servletPath);

//        String name = controllerObj.getClass().getName();
//        System.out.println(name);

        String operate = req.getParameter("operate");

        if (Tools.isEmpty(operate)) operate = "index";
        try {
            Method method = controllerObj.getClass().getDeclaredMethod(operate, HttpServletRequest.class);
            method.setAccessible(true);
            Object returnObj = method.invoke(controllerObj, req);
            String returnStr = (String) returnObj;
            if (returnStr.startsWith("redirect:")) {
                String newDirection = returnStr.substring("redirect:".length());
//                    System.out.println(newDirection);
                resp.sendRedirect(newDirection);

            } else {
                super.processTemplate(returnStr, req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
