package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.UserVO;

@Mapper
public interface UserMapper {
    void insertUser(UserVO user);
    boolean existsByEmail(@Param("email") String email);
    UserVO findByEmail(@Param("email") String email);
}
