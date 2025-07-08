package org.wit.hrmatching.mapper.employer;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.TechStackVO;

import java.util.List;

@Mapper
public interface TechStackMapper {
    List<TechStackVO> getAllTechStacks(); // 스택 이름 목록만 조회

    List<String> selectNamesByIds(@Param("stackIds")List<Long> ids);
}

