# 1 MyBatis

## 1.1 搭建环境

创建一个Maven项目，在pom.xml文件下导入所需的依赖：

```xml
    <dependencies>
        <!--mybatis 框架-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.14</version>
        </dependency>

        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.0.33</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.12</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.4.14</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-core</artifactId>
            <version>1.4.14</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
        </dependency>
    </dependencies>
```

## 1.2 配置环境

在配置框架环境之前，我们需要创建数据库，用于后续进行数据的操作。

```sql
CREATE DATABASE mybatisDB;

USE mybatisDB;

CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

INSERT INTO user (username, password) VALUES ('admin', 'admin');
```

在项目的resources资源文件夹下创建logback.xml和mybatis-config.xml来对日志和mybatis框架进行一个配置

logback.xml：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>[%level] %boldBlue(%d{HH:mm:ss.SSS}) %cyan([%thread]) %boldGreen(%logger{15}) - %msg %n</pattern>
        </encoder>
    </appender>
    <root level="debug">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```

mybatis-config.xml：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <typeAliases>
        <!-- 这行代码表示的是扫描指定包下的pojo类 -->
        <!-- 配置之后，在映射过程中填写数据类型，无需填写数据类型的包路径 -->
        <package name="cn.xumob.entity"/>
    </typeAliases>
    
    <!-- 这行代码是配置mybatis的数据源的 -->
    <!-- default属性指的是对应的数据源id，当项目运行时，mybatis会默认使用对应的数据源 -->
    <!-- mybatis是允许有多个environment元素的 -->
    <environments default="development">
        <environment id="development">
            <!-- 这行代码是配置事务管理器的类型的，我们使用的是Java来编写的项目， 所以正常使用的是JDBC，这里这样写就行了 -->
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatisDB?useSSL=false"/>
                <property name="username" value="root"/>
                <property name="password" value="qaz98188729"/>
            </dataSource>
        </environment>
    </environments>
    
    <!-- 这行代码是扫描对应的映射文件的，也叫映射器 -->
    <!-- 它是通过Mapper映射文件来进行对sql的映射的 -->
    <!-- 在sql映射前，我们需要创建映射关系，来进行sql映射 -->
    <mappers>
        <mapper resource="mapper/UserMapper.xml"/>
    </mappers>
</configuration>
```

## 1.3 Mapper代理开发

在进行sql映射之前，我们需要定义一个Mapper映射器，来创建sql的映射关系，达到通过xml来调用sql语句。

首先在resources资源文件夹下，创建一个Mapper文件夹，为了保证项目的层次结构，所以需要创建一个文件夹来保存这些映射器。

我们创建一个名为UserMapper.xml的文件夹，来进行sql映射，获取数据库中用户的信息。

UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!-- 这里是Mapper映射器的头部代码，这个很关键，这个是创建Mapper映射器的 -->
<!-- 如果没有这个头部代码，就无法正常创建映射器 -->
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 这行代码是定义映射器的，namespace命名空间代表了你在项目中对应的哪个包下的Mapper接口 -->
<!-- 让Mapper接口和Mapper映射器达成一种映射关系 -->
<mapper namespace="cn.xumob.mapper.UserMapper">
    <!-- 这行代码定义了一个查询，这里可以编写sql的查询语句，通过resultType属性定义的数据类型，后续返回的数据将封装至该类型中 -->
    <select id="selectUser" resultType="User">
        select * from user
    </select>
</mapper>
```

我们接下来可以创建一个Mapper接口，让它们达成一个映射关系，方便后续调用sql语句

UserMapper.java

```java
public interface UserMapper {
    
    //记住，不用加public，因为接口下的所有抽象方法都是默认public修饰符
    List<User> selectUser();
}
```

## 1.4 数据查询

我们已经配置好sql映射了，我们现在需要在Main类中来开启mybatis的sqlSession会话，来开启映射。

Main.java

```java
public class Main {
    public static void main(String[] args) {
        // 这行代码是填写mybatis的配置文件路径用的
        // 示例: **/mybatis-config.xml
        // 但由于我的配置文件是直接创建在resources文件夹下的，所以不需要加什么前缀
        String config = "mybatis-config.xml";
        // 这行代码是通过资源流来获取配置文件的，资源流对应的文件夹就是resources
        // 所以我们可以通过资源流来获取resources下的所有文件
        InputStream inputStream = Resources.getResourceAsStream(config);
        // 通过获取到的配置文件，来构建sql会话
        SqlSessionFactory factory = new SqlSessionFactoryBuilder.build(inputStream);
        
        // 我们在获取映射关系前，一定要打开sql会话，不然无法执行mybatis的映射操作
        SqlSession sqlSession = factory.openSession();
        // 这行代码就是用于获取mapper的映射关系的
        // 它是通过指定的Mapper接口的class对象来获取对应Mapper映射器，来形成映射关系的
        // Mapper映射器下的所有sql语句全部都映射到了Mapper接口下的抽象方法，当我们调用抽象方法时，将会直接执行sql语句
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        // 我们在Mapper映射器中定义了一个查询，当我们进行查询的时候，将会把返回的数据封装至我们之前在resultType属性定义的数据类型中
        // 如果返回的是多条数据，mybatis将会把类型改为对应的数据类型集合来存储多条数据
        List<User> users = userMapper.selectUser();
        
        users.forEach(System.out::println);
    }
}
```

## 1.5 参数占位符

参数占位符在mybatis中作用于sql语句中的，它可以将我们提供的一些参数拼接到sql语句中，但是mybatis中存在两种参数占位符。

这个要记住，因为这个是很重要的Java面试题。

mybatis中有两个参数占位符，一个是 #{}，还有一个是 ${}

**那它们的区别是什么呢？**

1. ${} ：这是一个占位符，它是用于参数拼接的，但是它有一个缺点就是，它是直接将参数拼接到sql语句中的，所以它会有一个sql注入的风险。
2. #{} ：这也是一个占位符，但是它的执行逻辑不一样，它是先将 ? 占位符拼接到了sql语句中，然后mybatis通过 ? 占位符来设定指定的参数值，这种方式大大降低了sql注入的风险

这就是它们的区别。

所以我们普遍都会选择 #{} 占位符来进行参数拼接。

**它们的使用时机**

1. ${} ：当表名或者列名不固定的情况下可进行使用
2. #{} ：当进行参数传递的时候可以进行使用

## 1.6 结果映射

### 1.6.1 建立结果映射关系

当我们建立好mapper之间的关系后，是可以正常查询所有结果了，但是有没有想过一个问题，就是当数据表中的列名与pojo类中的属性名不相同时，该如何处理呢?

mybatis为了用户们不会出现这样的问题，创造了结果映射 (Result Map)。

结果映射就是用来解决这个问题的，你可以通过定义结果映射来将列名和属性名进行一个关联，将数据封装到对应有关联的属性中。

UserMapper.java

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.xumob.mapper.UserMapper">
    <!--定义一个结果映射，来对列名和属性名进行一个关联-->
    <!-- column指的是列名，property指的是属性名 -->
    <resultMap id="userResultMap" type="User">
        <id column="id" property="id" />
        <result column="user_name" property="username" />
        <result column="password" property="passWord" />
    </resultMap>
    
    <!-- 这里可以省略掉之前的resultType了，因为类型已经在resultMap中定义了 -->
    <select id="selectUser" resultMap="userResultMap">
        SELECT * FROM user
    </select>
</mapper>
```

### 1.6.2 多对一结果映射 (association)

那有没有想过，当我们一个表的一行数据根某个用户有关系的时候，我们该怎么做呢？

是不是想过多对一查询？

我们原本在Java进行多对一查询的时候很麻烦，需要查询一行数据之后封装到一个实体类里，然后再将另外一部分的数据封装到另外一个实体类中，

这种方式大大降低了多对一的查询效率，我们可以使用mybatis结果映射中自带的多对一查询功能（association）标签，

association 可以方便我们进行多对一查询，可以将另一条数据封装到一个实体类中。

接下来演示一下

User.java

```java
/** 以下注解使用到了lombok **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    
    private String username;
    
    private String password;
    
    private UserInfo userInfo;
}
```

UserInfo.java

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private Integer id;
    
	private String name;
    
    private String age;
}
```

UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.xumob.mapper.UserMapper">
    
    <resultMap id="userInfoResultMap" type="UserInfo">
        <id property="id" column="userInfo_id" />
        <result property="name" column="name" />
        <result property="age" column="age" />
    </resultMap>
    
    <resultMap id="userResultMap" type="User">
        <id property="id" column="id" />
        <result property="username" column="username" />
        <result property="password" column="password" />
        <association property="userInfo" javaType="UserInfo" resultMap="userInfoResultMap" />
    </resultMap>
    
    <select id="selectUser" resultMap="userResultMap">
    	SELECT user.id, user.username, user.password, userInfo.id AS userInfo_id, userInfo.name, userInfo.age
        FROM user
        LEFT JOIN userInfo
        ON user.id = userInfo.id
        WHERE user.id = #{id};
    </select>
</mapper>
```

这是执行之后的效果

```
User(id=1, username=admin, password=admin, userInfo=UserInfo(id=1, name=zhangsan, age=18))
```

### 1.6.3  一对多结果映射 (collection)

当我们一条用户数据与多个数据有关系的时候该如何处理呢？

例如：一个用户有多个订单的时候

我们就可以使用到mybatis的 collection 标签，它可以帮助我们进行一对多查询，将多条订单数据封装到用户实体类下。

Order.java

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer id;
    
    private String productName;
}
```

User.java

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    
    private String username;
    
    private String password;
    
    private List<Order> orders;
}
```

UserMapper.java

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.xumob.mapper.UserMapper">
    
    <resultMap id="orderResultMap" type="Order">
        <id property="id" column="order_id" />
        <result property="productName" column="product_name" />
    </resultMap>
    
    <resultMap id="userResultMap" type="User">
        <id property="id" column="id" />
        <result property="username" column="username" />
        <result property="password" column="password" />
        <collection property="orders" ofType="Order" resultMap="orderResultMap" />
    </resultMap>
    
    <select id="selectUserWithOrders" resultMap="userResultMap">
        SELECT user.id, user.username, user.password, `order`.id AS order_id, `order`.product_name
        FROM user
        LEFT JOIN `order`
        ON user.id = `order`.id
        WHERE user.id = #{id}
    </select>
</mapper>
```

执行之后的效果

```
User(id=1, username=admin, password=admin, orders=[Order(id=1, productName=玩具车, userId=1), Order(id=2, productName=玩具枪, userId=1), Order(id=3, productName=玩具熊, userId=1), Order(id=4, productName=玩具猴, userId=1)])
```

### 1.6.4 关联的嵌套结果映射

当我们发布一条博客的时候会包含博客和作者的信息，那sql语句就是以下查询语句

```xml
<select id="selectBlogWithAuthor" resultType="Blog">
    SELECT
        blog.id AS blog_id,
        blog.title AS blog_title,
        blog.author_id AS author_id,
        author.username AS author_username,
        author.password AS author_password,
        author.email AS author_email,
    FROM blog
    LEFT JOIN author
    ON blog.id = author.id
    WHERE blog.author_id = #{id}
</select>
```

```xml
<resultMap id="blogResultMap" type="Blog">
    <id property="id" column="blog_id" />
    <result property="title" column="blog_title" />
    <association property="author" javaType="Author" resultMap="authorResultMap" />
</resultMap>

<resultMap id="authorResultMap">
    <id property="id" column="author_id" />
    <result property="username" column="author_username" />
    <result property="password" column="author_password" />
    <result property="email" column="author_email" />
</resultMap>
```

这是非常简单的一个多对一的结果映射，这里使用的是一个外部的结果映射元素来进行多对一映射。

有一个重点，id在结果映射是一个非常重要的元素，你起码要指定一个或多个这样的元素做为数据标识。

这里Author结果映射是外部结果映射元素，外部的结果映射元素是可以被重用的，但是我们这里暂时还不重用Author结果映射，那我们可以用到嵌套的结果映射元素来进行多对一映射。

```xml
<resultMap id="blogResultMap" type="Blog">
    <id property="id" column="blog_id" />
    <result property="title" column="blog_title" />
    <association property="author" javaType="Author">
    	<id property="id" column="author_id" />
    	<result property="username" column="author_username" />
    	<result property="password" column="author_password" />
    	<result property="email" column="author_email" />
    </association>
</resultMap>
```

那如果博客有共同创作的作者呢？那我们可以在数据表中多加一个共同创作的列，所以查询语句如下：

```xml
<select id="selectBlogWithAuthor" resultMap="blogResultMap">
    SELECT
        blog.id AS blog_id,
        blog.title AS blog_title,
        author.id AS author_id,
        author.username AS author_username,
        author.password AS author_password,
        author.email AS author_email,
    	CA.id AS ca_author_id,
    	CA.username AS co_author_username,
    	CA.password AS co_author_password,
    	CA.email AS co_author_email
    FROM blog
    LEFT OUTER JOIN author
    ON blog.author_id = author.id
    LEFT OUTER JOIN author CA
    ON blog.co_author_id = CA.id
    WHERE blog.id = #{id}
</select>
```

那我们多了一个共同创作者，由于列名跟结果映射中的列名不同，所以我们需要用到 **columnPrefix** 来给列名添加前缀，使其能正常对 **co_author** 进行结果映射。

```xml
<resultMap id="blogResultMap" type="Blog">
    <id property="id" column="blog_id" />
    <result property="title" column="blog_title" />
    <association property="author" javaType="Author" resultMap="authorResultMap" />
    <association property="coAuthor" javaType="Author" resultMap="authorResultMap" columnPrefix="co_" />
</resultMap>

<resultMap id="authorResultMap">
    <id property="id" column="author_id" />
    <result property="username" column="author_username" />
    <result property="password" column="author_password" />
    <result property="email" column="author_email" />
</resultMap>
```

## 1.7 条件查询

我们之前实现了通过建立映射关系来调用sql语句进行查询数据，但是我们要条件查询一条数据该怎么做？我该如何进行参数拼接达到条件查询？

我们可以在UserMapper.xml下写一个查询来进行条件查询

UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.xumob.mapper.UserMapper">
    <resultMap id="userResultMap" type="User">
        <id column="id" property="id" />
        <result column="user_name" property="username" />
        <result column="password" property="passWord" />
    </resultMap>
    
    <select id="listUser" resultMap="userResultMap">
        SELECT * FROM user
    </select>
    
    <!-- 定义一个getUser查询 -->
    <!-- 通过使用参数占位符来设定指定的参数值，来进行条件查询 -->
    <select id="getUser" resultMap="userResultMap">
        SELECT * FROM user
        <where>
            AND username LIKE #{username}
        </where>
    </select>
</mapper>
```

我们现在就需要去Mapper接口定义一个指定的抽象方法来进行条件查询。

UserMapper.java

```java
package cn.xumob.mapper;

import cn.xumob.entity.User;

import java.util.List;

public interface UserMapper {

    List<User> listUser();
        
    User getUser(String username);

}

```

现在我们就成功的定义了一个条件查询，我们创建一个单元测试来测试这个条件查询

TestUser.java

```java
public class TestUser {
    @Test
    public void getUserById() {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = build.openSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        User user = mapper.getUser();

        System.out.println(user);

        sqlSession.close();
    }
}
```

## 1.8 动态 SQL

