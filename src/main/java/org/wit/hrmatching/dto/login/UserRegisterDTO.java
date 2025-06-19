package org.wit.hrmatching.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
<<<<<<< HEAD
=======
import jakarta.validation.constraints.Size;
>>>>>>> origin/dev
import lombok.Data;

@Data
public class UserRegisterDTO {

<<<<<<< HEAD
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
=======
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자리 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력하세요.")
    private String confirmPassword;

    @NotBlank(message = "이름은 필수입니다.")
>>>>>>> origin/dev
    private String name;

    @NotNull
    private String role;
}
