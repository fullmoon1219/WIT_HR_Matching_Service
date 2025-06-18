package org.wit.hrmatching.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dto.login.UserRegisterDTO;
import org.wit.hrmatching.enums.UserRole;
import org.wit.hrmatching.mapper.UserMapper;
import org.wit.hrmatching.vo.UserVO;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegisterDTO dto) {
        if (userMapper.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }

        UserVO user = new UserVO();

        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setRole(UserRole.valueOf(dto.getRole()).name());
        userMapper.insertUser(user);
    }

    public void saveOrUpdate(UserVO user) {
        UserVO existing = userMapper.findByEmail(user.getEmail());
        if (existing == null) {

            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userMapper.insertUser(user);
        }
    }


}
