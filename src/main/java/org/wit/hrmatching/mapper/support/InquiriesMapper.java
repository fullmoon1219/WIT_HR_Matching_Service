package org.wit.hrmatching.mapper.support;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.InquiryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiriesMapper {

    int countAll();
    int countByStatus(String status);

    List<InquiryVO> selectAll(@Param("status") String status,
                              @Param("keyword") String keyword,
                              @Param("offset") int offset,
                              @Param("limit") int limit);

    int selectTotalCount(@Param("status") String status,
                         @Param("keyword") String keyword);

    int updateStatus(@Param("inquiryIds") List<Long> inquiryIds,
                     @Param("status") String status);

    int deleteInquiries(@Param("inquiryIds") List<Long> inquiryIds);

    void updateReply(@Param("id") Long inquiryId, @Param("reply") String reply);

    void deleteReply(@Param("id") Long id);

}
