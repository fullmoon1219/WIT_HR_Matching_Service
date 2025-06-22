package org.wit.hrmatching.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.LoginHistoryMapper;
import org.wit.hrmatching.vo.LoginHistoryVO;

@Repository
@RequiredArgsConstructor
public class LoginHistoryDAO {

    private final LoginHistoryMapper loginHistoryMapper;

    public void insertLoginHistory(LoginHistoryVO loginHistoryVO) {
        loginHistoryMapper.insertLoginHistory(loginHistoryVO);
    }
}
