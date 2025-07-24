package org.wit.hrmatching.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.admin.AdminMemoVO;

import java.util.List;

@Mapper
public interface AdminMemoMapper {
    void insertMemo(AdminMemoVO memo);
    void deleteMemo(Long id);
    List<AdminMemoVO> getAllMemos();
}
