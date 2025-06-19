package org.wit.hrmatching.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.UserMapper;
import org.wit.hrmatching.mapper.ApplicantProfileMapper;
import org.wit.hrmatching.mapper.EmployerProfileMapper;
import org.wit.hrmatching.vo.UserVO;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.EmployerProfilesVO;

@Repository
@RequiredArgsConstructor
public class UserDAO {

    private final UserMapper userMapper;
    private final ApplicantProfileMapper applicantProfileMapper;
    private final EmployerProfileMapper employerProfileMapper;

    public boolean existsByEmail(String email) {
        return userMapper.existsByEmail(email);
    }

    public UserVO findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public void insertUser(UserVO user) {
        userMapper.insertUser(user);
    }

    // 유저와 함께 지원자 프로필 정보 가져오기
    public UserVO findUserWithApplicantProfile(Long userId) {
        UserVO user = userMapper.findByUserId(userId);
        if (user != null && "APPLICANT".equals(user.getRole())) {
            ApplicantProfilesVO profile = applicantProfileMapper.findByUserId(userId);
            user.setApplicantProfile(profile);  // 지원자 프로필 추가
        }
        return user;
    }

    // 유저와 함께 고용자 프로필 정보 가져오기
    public UserVO findUserWithEmployerProfile(Long userId) {
        UserVO user = userMapper.findByUserId(userId);
        if (user != null && "EMPLOYEE".equals(user.getRole())) {
            EmployerProfilesVO profile = employerProfileMapper.findByUserId(userId);
            user.setEmployerProfile(profile);  // 고용자 프로필 추가
        }
        return user;
    }
}
