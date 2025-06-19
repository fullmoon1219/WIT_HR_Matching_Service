package org.wit.hrmatching.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.config.auth.CustomUserDetails;
import org.wit.hrmatching.dao.ProfileDAO;
import org.wit.hrmatching.dao.UserDAO;
import org.wit.hrmatching.dto.login.UserRegisterDTO;
import org.wit.hrmatching.enums.UserRole;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.UserVO;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final ProfileDAO profileDAO;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegisterDTO dto) {
        if (userDAO.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }

        UserVO user = new UserVO();

        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setRole(UserRole.valueOf(dto.getRole()).name());
        userDAO.insertUser(user);

        // 유저 ID가 할당되었는지 확인 (자동 증가된 ID가 정상적으로 할당되어야 함)
        if (user.getId() == null) {
            throw new IllegalStateException("User ID could not be set after insertion");
        }

        // 역할에 따라 지원자 또는 기업 프로필 생성
        if ("APPLICANT".equals(dto.getRole())) {
            System.out.println("Applicant 생성");
            createApplicantProfile(user);  // 지원자 프로필 생성
        } else if ("EMPLOYER".equals(dto.getRole())) {
            System.out.println("Employer 생성");
            createEmployerProfile(user);  // 기업 프로필 생성
        }
    }

    public void saveOrUpdate(UserVO user) {
        UserVO existing = userDAO.findByEmail(user.getEmail());
        if (existing == null) {

            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userDAO.insertUser(user);
        }
    }

    public UserVO findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public UserVO getUserWithProfile(Long userId) {
        UserVO user = userDAO.findUserWithApplicantProfile(userId);  // 기본적으로 지원자 프로필 조회

        // 지원자가 아닌 경우, 기업 프로필로 설정
        if (user == null || !user.getRole().equals("APPLICANT")) {
            user = userDAO.findUserWithEmployerProfile(userId);  // EMPLOYEE일 경우 기업 프로필 조회
        }

        return user;
    }

    // 지원자 프로필 생성
    private void createApplicantProfile(UserVO user) {
        ApplicantProfilesVO profile = new ApplicantProfilesVO();
        profile.setUserId(user.getId());  // 유저의 ID 설정
        profile.setAge(0);  // 기본값 설정 (필요시 수정 가능)
        profile.setAddress("");  // 기본값 설정 (필요시 수정 가능)
        profile.setPhoneNumber("");  // 기본값 설정 (필요시 수정 가능)
        profile.setGender("OTHER");  // 기본값 설정
        profile.setPortfolioUrl("");  // 기본값 설정
        profile.setSelfIntro("");  // 기본값 설정
        profile.setJobType("FULLTIME");  // 기본값 설정
        profile.setExperienceYears(0);  // 기본값 설정
        profileDAO.createApplicantProfile(profile);  // 프로필 삽입
    }

    // 기업 프로필 생성
    private void createEmployerProfile(UserVO user) {
        EmployerProfilesVO profile = new EmployerProfilesVO();
        profile.setUserId(user.getId());  // 유저의 ID 설정
        profile.setCompanyName("");  // 기본값 설정
        profile.setBusinessNumber("");  // 기본값 설정
        profile.setAddress("");  // 기본값 설정
        profile.setPhoneNumber("");  // 기본값 설정
        profile.setHomepageUrl("");  // 기본값 설정
        profile.setIndustry("");  // 기본값 설정
        profile.setFoundedYear(0);  // 기본값 설정
        profile.setCompanySize("");  // 기본값 설정
        profileDAO.createEmployerProfile(profile);  // 기업 프로필 삽입
    }
}
