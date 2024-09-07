package cn.xumob.mapper;

import cn.xumob.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    User selectUser(@Param("id") int id);

}
