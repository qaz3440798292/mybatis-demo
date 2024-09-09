package cn.xumob.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;

    private String username;

    private String password;

    private UserInfo userInfo;

    private List<Order> orders;
}
