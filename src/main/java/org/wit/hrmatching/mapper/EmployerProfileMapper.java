package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.EmployerProfilesVO;

@Mapper
public interface EmployerProfileMapper {

    // 유저 ID로 기업 프로필 조회
    EmployerProfilesVO findByUserId(@Param("userId") Long userId);

    // 기업 프로필 삽입
    void insertEmployerProfile(EmployerProfilesVO profile);
}
