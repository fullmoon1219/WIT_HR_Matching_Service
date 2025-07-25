package org.wit.hrmatching.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dto.login.UserRegisterDTO;
import org.wit.hrmatching.enums.UserRole;
import org.wit.hrmatching.mapper.ProfileMapper;
import org.wit.hrmatching.mapper.UserMapper;
import org.wit.hrmatching.service.mail.MailService;
import org.wit.hrmatching.vo.user.ApplicantProfilesVO;
import org.wit.hrmatching.vo.user.EmployerProfilesVO;
import org.wit.hrmatching.vo.user.UserVO;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfileMapper profileMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public void registerUser(UserRegisterDTO dto) {
        if (userMapper.existsByEmail(dto.getEmail())) {
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

        /*
        * 실제 테스트 할 때 메일 전송 켜기.
        */
        insertUserWithProfile(user, true);
    }

    public void saveOrUpdate(UserVO user) {
        UserVO existing = userMapper.findByEmail(user.getEmail());
        if (existing != null) {
            if (!user.isSocialUser()) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("conflict", "이미 일반 회원으로 가입된 이메일입니다. 소셜 로그인으로 가입할 수 없습니다.", null)
                );
            }

            return;
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiration(null);

        insertUserWithProfile(user, false);
    }

    private void insertUserWithProfile(UserVO user, boolean sendMailNeeded) {
        userMapper.insertUser(user);

        if (user.getId() == null) {
            throw new IllegalStateException("User ID가 생성되지 않았습니다.");
        }

        if (sendMailNeeded) {
            mailService.sendVerificationMail(user.getEmail(), user.getVerificationToken());
        }

        if ("APPLICANT".equals(user.getRole())) {
            createApplicantProfile(user);
        } else if ("EMPLOYER".equals(user.getRole())) {
            createEmployerProfile(user);
        }
    }


    public UserVO findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public UserVO getUserWithProfile(Long userId) {
        UserVO user = userMapper.findByUserId(userId);

        if (user == null) {
            return null;
        }

        String role = user.getRole();

        if ("APPLICANT".equalsIgnoreCase(role)) {
            ApplicantProfilesVO profile = profileMapper.applicantFindByUserId(userId);
            user.setApplicantProfile(profile);
        } else if ("EMPLOYER".equalsIgnoreCase(role)) {
            EmployerProfilesVO profile = profileMapper.employerFindByUserId(userId);
            user.setEmployerProfile(profile);
        }

        return user;
    }

    // 지원자 프로필 생성
    private void createApplicantProfile(UserVO user) {
        ApplicantProfilesVO profile = new ApplicantProfilesVO();
        profile.setUserId(user.getId());  // 유저의 ID 설정
        profile.setAge(0);  // 기본값 설정
        profile.setAddress("");  // 기본값 설정
        profile.setPhoneNumber("");  // 기본값 설정
        profile.setGender("OTHER");  // 기본값 설정
        profile.setPortfolioUrl("");  // 기본값 설정
        profile.setSelfIntro("");  // 기본값 설정
        profile.setJobType("FULLTIME");  // 기본값 설정
        profile.setExperienceYears(0);  // 기본값 설정
        profileMapper.insertApplicantProfile(profile);  // 프로필 삽입
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
        profileMapper. insertEmployerProfile(profile);  // 기업 프로필 삽입
    }

    public UserVO findByVerificationToken(String token) {
        return userMapper.findByToken(token);
    }

    public void updateUser(UserVO user) {
        userMapper.updateUser(user);  // emailVerified, token, tokenExpiration 업데이트
    }

    public void deleteUserAccount(Long userId) {
        UserVO user = userMapper.findByUserId(userId);

        if (user == null) throw new RuntimeException("사용자 없음");

        // 지원자 프로필 존재 여부 확인 후 삭제
        if (profileMapper.existsApplicantProfile(userId)) {
            profileMapper.deleteApplicantProfileByUserId(userId);
        }

        // 기업 프로필 존재 여부 확인 후 삭제
        if (profileMapper.existsEmployerProfile(userId)) {
            profileMapper.deleteEmployerProfileByUserId(userId);
        }

        userMapper.softDeleteUserById(userId);
    }


    public void updateLastLoginTime(Long userId) {
        userMapper.updateLastLogin(userId);
    }

    // 이메일 인증 토큰 재생성 및 이메일 재전송
    public void resendVerificationEmail(String email) {
        UserVO user = userMapper.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
        }

        if (user.isEmailVerified()) {
            throw new IllegalStateException("이미 이메일 인증이 완료된 사용자입니다.");
        }

        // 새 토큰 생성 및 저장
        String newToken = UUID.randomUUID().toString();
        LocalDateTime newExpiration = LocalDateTime.now().plusMinutes(30);

        user.setVerificationToken(newToken);
        user.setTokenExpiration(newExpiration);
        userMapper.updateVerificationToken(user);

        // 이메일 재전송
        mailService.sendVerificationMail(email, newToken);
    }

}
