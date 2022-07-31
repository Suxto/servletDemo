package com.servletdemo.myspringmvc.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContext implements BeanFactory {
    private final Map<String, Object> beanMap = new HashMap<>();

    public ClassPathXmlApplicationContext() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/applicationContext.xml");
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
//                    JDK-8
//                    Class beanClass = Class.forName(className);
//                    beanObj = beanClass.newInstance();
                    beanMap.put(beanId, beanObj);
                }
            }
            //确定bean之间的引用关系
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    NodeList childNodes = beanElement.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node childNode = childNodes.item(j);
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                            String childNodeName = childNode.getNodeName();
                            if ("property".equals(childNodeName)) {
                                Element propertyElement = (Element) childNode;
                                String propertyName = propertyElement.getAttribute("name");
                                String propertyRef = propertyElement.getAttribute("ref");
//                              找 ref对应的实例
                                Object refObj = beanMap.get(propertyRef);
                                Object beanObj = beanMap.get(beanId);
                                Field propertyField = beanObj.getClass().getDeclaredField(propertyName);
                                propertyField.setAccessible(true);
                                propertyField.set(beanObj, refObj);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
