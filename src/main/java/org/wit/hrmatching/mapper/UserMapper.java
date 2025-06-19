package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.UserVO;

@Mapper
public interface UserMapper {

    // 유저 정보 삽입
    void insertUser(UserVO user);

    // 이메일로 유저 존재 여부 체크
    boolean existsByEmail(@Param("email") String email);

    // 이메일로 유저 조회
    UserVO findByEmail(@Param("email") String email);

    // 유저 ID로 유저 정보 조회
    UserVO findByUserId(@Param("userId") Long userId);
}
