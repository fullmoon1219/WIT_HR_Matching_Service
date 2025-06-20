package org.wit.hrmatching.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Alias("UserVO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String role;
    private LocalDateTime createAt;
    private String status;  // "ACTIVE", "SUSPENDED", "WITHDRAWN"
    private LocalDateTime lastLogin;
    private String profileImage;
    private boolean emailVerified;
    private String verificationToken;
    private LocalDateTime tokenExpiration;
    private LocalDateTime updatedAt;
    private String loginType; // "EMAIL", "NAVER", "GOOGLE"

    private ApplicantProfilesVO applicantProfile;
    private EmployerProfilesVO employerProfile;

    public boolean isSocialUser() {
        return this.password == null;
    }
}
