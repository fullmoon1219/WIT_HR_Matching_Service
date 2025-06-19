package org.wit.hrmatching.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.UserMapper;
import org.wit.hrmatching.vo.UserVO;

@Repository
@RequiredArgsConstructor
public class UserDAO {

    private final UserMapper userMapper;

    public boolean existsByEmail(String email) {
        return userMapper.existsByEmail(email);
    }

    public UserVO findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public void insertUser(UserVO user) {
        userMapper.insertUser(user);
    }
}
