package com.servletdemo.user.service.impl;

import com.servletdemo.user.bean.User;
import com.servletdemo.user.service.UserService;
import com.servletdemo.utils.JDBC;
import com.servletdemo.utils.Tools;

import java.util.List;

public class UserServiceImpl implements UserService {

    @Override
    public List<User> getUserList(String keyword, String pageNo) {
        String sql;
        if (Tools.isnEmpty(keyword)) {
            sql = "select * from users where uName rlike ?  limit ? , 6";
            return JDBC.getForList(User.class, sql, keyword, (Integer.parseInt(pageNo) - 1) * 6);
        } else {
            sql = "select * from users limit ? , 6";
            return JDBC.getForList(User.class, sql, (Integer.parseInt(pageNo) - 1) * 6);
        }
    }

    @Override
    public int getPageCount(String keyword) {
        String sql;
        int recordNum;
        if (Tools.isnEmpty(keyword)) {

            sql = "select count(*) from users where uName rlike ? ;";
            recordNum = (int) JDBC.getCount(sql, keyword);
        } else {
            sql = "select count(*) from users";
            recordNum = (int) JDBC.getCount(sql);
        }
        return (recordNum + 5) / 6;
    }

    @Override
    public void addUser(String uName, String uAge, String uTel) {
        String sql = "insert into users (uName,uAge,uTel) values (?,?,?)";
        JDBC.upDate(sql, uName, uAge, uTel);
    }

    @Override
    public User getUserById(String id) {
        String sql = "select * from users where id = ?";
        return JDBC.getOne(User.class, sql, id);
    }

    @Override
    public void updateUser(User user, String uName, String uAge, String uTel) {
        user.setuName(uName);
        user.setuAge(Integer.parseInt(uAge));
        user.setuTel(uTel);
        String sql = "update users set uName = ?, uAge = ?, uTel = ? where id = ?";
        JDBC.upDate(sql, user.getuName(), user.getuAge(), user.getuTel(), user.getId());
    }

    @Override
    public void removeUser(User user) {
        int id = user.getId();
        String sql = "delete from users where id = ?";
        JDBC.upDate(sql, id);
    }
}
