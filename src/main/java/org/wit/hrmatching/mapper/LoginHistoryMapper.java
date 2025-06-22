package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.LoginHistoryVO;

@Mapper
public interface LoginHistoryMapper {

    void insertLoginHistory(LoginHistoryVO history); // 로그인 기록 삽입
}
