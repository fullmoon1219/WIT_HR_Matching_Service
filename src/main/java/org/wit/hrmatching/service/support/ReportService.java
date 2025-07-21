package org.wit.hrmatching.service.support;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wit.hrmatching.dto.support.CommentDTO;
import org.wit.hrmatching.dto.support.JobPostDTO;
import org.wit.hrmatching.dto.support.SupportPostDetailDTO;
import org.wit.hrmatching.dto.support.UserDetailDTO;
import org.wit.hrmatching.mapper.support.ReportMapper;
import org.wit.hrmatching.vo.support.ReportVO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportMapper reportMapper;

    public Page<ReportVO> getPagedReports(Pageable pageable, String reportType, String status) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("reportType", reportType);
        filter.put("status", status);
        filter.put("offset", pageable.getOffset());
        filter.put("size", pageable.getPageSize());

        List<ReportVO> list = reportMapper.getReportList(filter);
        int total = reportMapper.getReportTotalCount(filter);

        return new PageImpl<>(list, pageable, total);
    }

    public Map<String, Object> getReportStats() {
        return reportMapper.getReportStats();
    }

    public void registerReport(ReportVO report) {
        reportMapper.insertReport(report);
    }

    @Transactional
    public void applyReportAction(Long reportId, String action, Long adminId) {
        ReportVO report = new ReportVO();
        report.setId(reportId);
        report.setStatus("REVIEWED");
        report.setReviewedByAdminId(adminId);
        report.setReviewedAt(LocalDateTime.now());
        report.setActionTaken(action); // ← 제재 내용 기록
        reportMapper.updateReportStatus(report);

        Long targetUserId = reportMapper.findReportedUserIdByReportId(reportId);

        if (action.startsWith("WARNING")) {
            reportMapper.increaseWarning(targetUserId);
            int currentWarning = reportMapper.getWarningCount(targetUserId);
            if (currentWarning >= 3) {
                reportMapper.suspendUserById(targetUserId);
            }
        } else if ("SUSPEND".equals(action)) {
            reportMapper.suspendUserById(targetUserId);
        }
    }

    @Transactional
    public void dismissReport(Long reportId, Long adminId) {
        ReportVO report = new ReportVO();
        report.setId(reportId);
        report.setStatus("DISMISSED");
        report.setReviewedByAdminId(adminId);
        report.setReviewedAt(LocalDateTime.now());
        report.setActionTaken("NONE"); // ← 기각 시 NONE으로 기록
        reportMapper.updateReportStatus(report);
    }

    public ReportVO getReportById(Long reportId) {
        return reportMapper.selectReportById(reportId);
    }


    public JobPostDTO getJobPostById(Long id) {
        return reportMapper.selectJobPostById(id);
    }

    public SupportPostDetailDTO getPostDetailById(Long id) {
        return reportMapper.selectPostDetailById(id);
    }

    public CommentDTO getCommentById(Long id) {
        return reportMapper.selectCommentById(id);
    }

}
