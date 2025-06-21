package org.wit.hrmatching.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.ProfileDAO;
import org.wit.hrmatching.dao.UserDAO;
import org.wit.hrmatching.dto.login.UserRegisterDTO;
import org.wit.hrmatching.enums.UserRole;
import org.wit.hrmatching.service.mail.MailService;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.UserVO;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDAO userDAO;
    private final ProfileDAO profileDAO;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public void registerUser(UserRegisterDTO dto) {
        if (userDAO.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        UserVO user = new UserVO();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setRole(UserRole.valueOf(dto.getRole()).name());
        user.setEmailVerified(false);
        user.setLoginType("EMAIL");
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setTokenExpiration(LocalDateTime.now().plusMinutes(30));

        insertUserWithProfile(user, true); // 인증 메일 전송 포함
    }

    public void saveOrUpdate(UserVO user) {
        UserVO existing = userDAO.findByEmail(user.getEmail());
        if (existing != null) {
            if (!user.isSocialUser()) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("conflict", "이미 일반 회원으로 가입된 이메일입니다. 소셜 로그인으로 가입할 수 없습니다.", null)
                );
            }

            return;
        }

        // 소셜 사용자는 이메일 인증이 이미 되었거나 필요 없음
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiration(null);

        insertUserWithProfile(user, false); // 인증 메일 전송 안 함
    }

    private void insertUserWithProfile(UserVO user, boolean sendMailNeeded) {
        userDAO.insertUser(user);

        if (user.getId() == null) {
            throw new IllegalStateException("User ID가 생성되지 않았습니다.");
        }

//        if (sendMailNeeded) {
//            mailService.sendVerificationMail(user.getEmail(), user.getVerificationToken());
//        }

        if ("APPLICANT".equals(user.getRole())) {
            createApplicantProfile(user);
        } else if ("EMPLOYER".equals(user.getRole())) {
            createEmployerProfile(user);
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

    public UserVO findByVerificationToken(String token) {
        return userDAO.findByToken(token);
    }

    public void updateUser(UserVO user) {
        userDAO.updateUser(user);  // emailVerified, token, tokenExpiration 업데이트
    }

    public void deleteUserAccount(Long userId) {
        UserVO user = userDAO.findUserById(userId);

        if (user == null) throw new RuntimeException("사용자 없음");

        if ("APPLICANT".equals(user.getRole())) {
            profileDAO.deleteApplicantProfileByUserId(userId);
        } else if ("EMPLOYER".equals(user.getRole())) {
            profileDAO.deleteEmployerProfileByUserId(userId);
        }

        userDAO.deleteUserById(userId);
    }
}
