package org.wit.hrmatching.mapper.support;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.dto.support.CommentDTO;
import org.wit.hrmatching.dto.support.JobPostDTO;
import org.wit.hrmatching.dto.support.SupportPostDetailDTO;
import org.wit.hrmatching.dto.support.UserDetailDTO;
import org.wit.hrmatching.vo.support.ReportVO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    List<ReportVO> getReportList(Map<String, Object> filter);
    int getReportTotalCount(Map<String, Object> filter);

    Map<String, Object> getReportStats();

    void insertReport(ReportVO report);

    void updateReportStatus(ReportVO report);          // 상태 + 처리자 저장
    Long findReportedUserIdByReportId(Long reportId);  // 신고된 유저 ID 조회
    void increaseWarning(Long userId);                 // 경고 1 증가
    int getWarningCount(Long userId);                  // 현재 경고 수
    void suspendUserById(Long userId);

    ReportVO selectReportById(Long reportId);

    JobPostDTO selectJobPostById(Long id);
    SupportPostDetailDTO selectPostDetailById(Long id);
    CommentDTO selectCommentById(Long id);

}
