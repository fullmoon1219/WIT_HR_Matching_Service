package org.wit.hrmatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public boolean emailExists(String email) {
        return userMapper.findByEmail(email) != null;
    }

    public int updatePassword(Long userId, String encodedPassword) {
        return userMapper.updatePassword(userId,encodedPassword);
    }
}
