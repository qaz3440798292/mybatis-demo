package cn.xumob;


import cn.xumob.utils.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;

public class Main {

    public static void main(String[] args) {
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession(true);

    }
}