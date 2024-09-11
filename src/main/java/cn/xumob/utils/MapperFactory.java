package cn.xumob.utils;

import org.apache.ibatis.session.SqlSession;

public class MapperFactory {
    public static <T> T getMapper(Class<T> clazz) {
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession(true);
        return sqlSession.getMapper(clazz);
    }
}
