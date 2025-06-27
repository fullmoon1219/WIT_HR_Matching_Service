package org.wit.hrmatching.mapper.employer;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

@Mapper
public interface EmployerProfileMapper {

    EmployerProfilesVO selectEmployerProfiles(Long userId);

    int updateEmployerProfiles(EmployerProfilesVO vo);


}
