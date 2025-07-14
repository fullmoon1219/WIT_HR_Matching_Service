package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.user.ApplicantProfilesVO;
import org.wit.hrmatching.vo.user.EmployerProfilesVO;

@Mapper
public interface ProfileMapper {

    // 유저 ID로 지원자 프로필 조회
    ApplicantProfilesVO applicantFindByUserId(@Param("userId") Long userId);

    // 지원자 프로필 삽입
    void insertApplicantProfile(ApplicantProfilesVO profile);

    void deleteApplicantProfileByUserId(Long userId);

    // 유저 ID로 기업 프로필 조회
    EmployerProfilesVO employerFindByUserId(@Param("userId") Long userId);

    // 기업 프로필 삽입
    void insertEmployerProfile(EmployerProfilesVO profile);

    void deleteEmployerProfileByUserId(Long userId);

    // 데이터 존재하는지 확인
    boolean existsApplicantProfile(Long userId);

    boolean existsEmployerProfile(Long userId);

}
