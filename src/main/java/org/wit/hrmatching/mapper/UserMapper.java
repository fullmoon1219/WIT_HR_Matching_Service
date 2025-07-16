package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.user.UserVO;

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

    // 토큰 검색
    UserVO findByToken(String token);

    // 이메일 인증 관련 정보 업데이트
    void updateUser(UserVO user);

    // 회원 탈퇴
    void deleteUserById(Long id);

    // 최종 로그인
    void updateLastLogin(Long id);

    int updatePassword(@Param("userId")Long userId, @Param("encodedPassword")String encodedPassword);

    void updateVerificationToken(UserVO user);

}
