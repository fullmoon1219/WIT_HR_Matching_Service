package org.wit.hrmatching.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.ApplicantProfileMapper;
import org.wit.hrmatching.mapper.EmployerProfileMapper;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.EmployerProfilesVO;

@Repository
@RequiredArgsConstructor
public class ProfileDAO {

    private final ApplicantProfileMapper applicantProfilesMapper;
    private final EmployerProfileMapper employerProfilesMapper;

    // 지원자 프로필 삽입
    public void createApplicantProfile(ApplicantProfilesVO profile) {
        applicantProfilesMapper.insertApplicantProfile(profile);
    }

    // 기업 프로필 삽입
    public void createEmployerProfile(EmployerProfilesVO profile) {
        employerProfilesMapper.insertEmployerProfile(profile);
    }
}
