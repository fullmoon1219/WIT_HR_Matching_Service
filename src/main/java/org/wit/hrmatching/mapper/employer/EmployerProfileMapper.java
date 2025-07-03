package org.wit.hrmatching.mapper.employer;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.EmployerRecentApplicantVO;
import org.wit.hrmatching.vo.JobPostVO;
import java.util.List;

@Mapper
public interface EmployerProfileMapper {

    EmployerProfilesVO selectEmployerProfiles(Long userId);
    boolean updateEmployerProfiles(EmployerProfilesVO employerProfilesVO);

    // User 테이블의 대표자 이름(name) 업데이트
    void updateUserName(@Param("userId") Long userId, @Param("name") String name);

    List<EmployerRecentApplicantVO> selectEmployerRecentApplicantList(Long userId);

}
