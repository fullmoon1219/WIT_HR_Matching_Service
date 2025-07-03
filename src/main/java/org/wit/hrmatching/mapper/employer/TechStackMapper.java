package org.wit.hrmatching.mapper.employer;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TechStackMapper {
    List<String> getAllTechStacks(); // 스택 이름 목록만 조회
}

