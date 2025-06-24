package org.wit.hrmatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.LoginHistoryMapper;
import org.wit.hrmatching.vo.LoginHistoryVO;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryMapper loginHistoryMapper;

    public void insertLoginHistory(LoginHistoryVO loginHistoryVO) {
        loginHistoryMapper.insertLoginHistory(loginHistoryVO);
    }
}
