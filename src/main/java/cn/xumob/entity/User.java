package cn.xumob.entity;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private int id;

    private String username;

    private String password;

    private UserInfo userInfo;

    private List<Order> orders;
}
