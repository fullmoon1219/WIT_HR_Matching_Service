package org.wit.hrmatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.UserDAO;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    public boolean emailExists(String email) {
        return userDAO.emailExists(email);
    }
}
