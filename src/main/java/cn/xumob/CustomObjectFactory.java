package cn.xumob;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class CustomObjectFactory extends DefaultObjectFactory {

    private final Logger logger = LoggerFactory.getLogger(CustomObjectFactory.class);

    public CustomObjectFactory() {
        super();
    }

    @Override
    public <T> T create(Class<T> type) {
        logger.warn("测试" + type.getName());
        return super.create(type);
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        return super.create(type, constructorArgTypes, constructorArgs);
    }

    @Override
    public <T> boolean isCollection(Class<T> type) {
        return super.isCollection(type);
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }
}
