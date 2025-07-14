package org.wit.hrmatching.service.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wit.hrmatching.mapper.employer.EmployerProfileMapper;
import org.wit.hrmatching.vo.user.EmployerProfilesVO;
import org.wit.hrmatching.vo.application.EmployerRecentApplicantVO;

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

    /**
     * 사용자 이름(user.name)과 기업 프로필(employer_profiles) 동시에 업데이트
     */
    @Transactional
    public void updateFullProfile(EmployerProfilesVO profile) {
        employerProfileMapper.updateEmployerProfiles(profile);
        employerProfileMapper.updateUserName(profile.getUserId(), profile.getCeoName());
    }

    public int updateProfileImage(Long userId, String storedName) {
        return employerProfileMapper.updateProfileImage(userId,storedName);
    }

}
