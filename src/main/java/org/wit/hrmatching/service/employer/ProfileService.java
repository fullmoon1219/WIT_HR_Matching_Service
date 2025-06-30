package org.wit.hrmatching.service.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.employer.ProfileDAO;
import org.wit.hrmatching.mapper.employer.EmployerProfileMapper;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.EmployerRecentApplicantVO;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final EmployerProfileMapper employerProfileMapper;

    public EmployerProfilesVO getEmployerProfile(Long userId) {
        return employerProfileMapper.selectEmployerProfiles(userId);
    }

    public boolean updateProfile(EmployerProfilesVO profile) {
        return employerProfileMapper.updateEmployerProfiles(profile);

    }

    public List<EmployerRecentApplicantVO> selectEmployerRecentApplicantList(Long userId) {
        return employerProfileMapper.selectEmployerRecentApplicantList(userId);
    }
}
