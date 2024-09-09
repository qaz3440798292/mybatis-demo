package cn.xumob;


import cn.xumob.entity.User;
import cn.xumob.mapper.UserMapper;
import cn.xumob.utils.SqlSessionFactoryUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) {
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession(true);

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        User user = mapper.selectUser(1);

        System.out.println(user);

        user.getOrders().forEach(System.out::println);

        sqlSession.close();
    }
}