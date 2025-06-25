package org.wit.hrmatching.mapper.community;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.community.BoardVO;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardVO> findAll();
}
