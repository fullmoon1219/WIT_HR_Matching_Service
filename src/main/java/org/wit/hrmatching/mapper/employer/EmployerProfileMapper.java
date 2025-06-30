package org.wit.hrmatching.mapper.employer;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.EmployerRecentApplicantVO;
import org.wit.hrmatching.vo.JobPostVO;
import java.util.List;

@Mapper
public interface EmployerProfileMapper {

    EmployerProfilesVO selectEmployerProfiles(Long userId);

    boolean updateEmployerProfiles(EmployerProfilesVO employerProfilesVO);


    List<EmployerRecentApplicantVO> selectEmployerRecentApplicantList(Long userId);
}
