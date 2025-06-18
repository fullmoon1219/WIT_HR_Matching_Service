package org.wit.hrmatching.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String role;
    private LocalDateTime createAt;
    private String status;
    private LocalDateTime lastLogin;
    private String profileImage;
    private boolean emailVerified;
    private boolean isActive;
    private LocalDateTime updatedAt;
}
