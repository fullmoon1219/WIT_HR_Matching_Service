package org.wit.hrmatching.mapper.support;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.InquiryReasonVO;

import java.util.List;

@Mapper
public interface InquiryReasonMapper {
    List<InquiryReasonVO> selectAll();
}
