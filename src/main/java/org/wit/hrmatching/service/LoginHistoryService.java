package org.wit.hrmatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.LoginHistoryDAO;
import org.wit.hrmatching.vo.LoginHistoryVO;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryDAO loginHistoryDAO;

    public void insertLoginHistory(LoginHistoryVO loginHistoryVO) {
        loginHistoryDAO.insertLoginHistory(loginHistoryVO);
    }
}
