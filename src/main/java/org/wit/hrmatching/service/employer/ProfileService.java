package org.wit.hrmatching.service.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.employer.ProfileDAO;
import org.wit.hrmatching.mapper.employer.EmployerProfileMapper;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final EmployerProfileMapper employerProfileMapper;
    private final ProfileDAO profileDAO;

    public EmployerProfilesVO getEmployerProfile(Long userId) {
        return employerProfileMapper.selectEmployerProfiles(userId);
    }

    public boolean updateProfile(EmployerProfilesVO vo) {
        return employerProfileMapper.updateEmployerProfiles(vo) > 0;
    }


}
