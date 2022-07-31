package com.servletdemo.user.service;

import com.servletdemo.user.bean.User;

import java.util.List;

public interface UserService {
    List<User> getUserList(String keyword, String pageNo);


    void addUser(String uName, String uAge, String uTel);

    User getUserById(String id);

    void removeUser(User user);

    int getPageCount(String keyword);

    void updateUser(User user, String uName, String uAge, String uTel);
}
