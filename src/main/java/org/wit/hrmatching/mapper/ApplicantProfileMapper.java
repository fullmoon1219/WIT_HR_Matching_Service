package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.ApplicantProfilesVO;

@Mapper
public interface ApplicantProfileMapper {

    // 유저 ID로 지원자 프로필 조회
    ApplicantProfilesVO findByUserId(@Param("userId") Long userId);

    // 지원자 프로필 삽입
    void insertApplicantProfile(ApplicantProfilesVO profile);
}
