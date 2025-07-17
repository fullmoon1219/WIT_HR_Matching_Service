package org.wit.hrmatching.mapper.support;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.support.InquiryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InquiriesMapper {

    int countAll();
    int countByStatus(String status);

    List<InquiryVO> selectAll(
            @Param("status") String status,
            @Param("keyword") String keyword,
            @Param("reasonId") Long reasonId,
            @Param("offset") int offset,
            @Param("size") int size
    );

    int selectTotalCount(
            @Param("status") String status,
            @Param("keyword") String keyword,
            @Param("reasonId") Long reasonId
    );

    int updateStatus(@Param("inquiryIds") List<Long> inquiryIds,
                     @Param("status") String status);

    int deleteInquiries(@Param("inquiryIds") List<Long> inquiryIds);

    void deleteReply(@Param("id") Long id);

    InquiryVO selectById(Long id);

    void updateReplyAndStatus(InquiryVO inquiry);

    List<Map<String, Object>> countByReason();

    void insertInquiry(InquiryVO inquiryVO);

    List<InquiryVO> getInquiriesByUserId(@Param("userId") Long userId,
                                         @Param("size") int size,
                                         @Param("offset") int offset);

    long countInquiriesByUserId(@Param("userId") Long userId);

}
