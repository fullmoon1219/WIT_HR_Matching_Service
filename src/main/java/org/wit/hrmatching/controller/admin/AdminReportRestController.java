package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
import org.wit.hrmatching.dto.support.CommentDTO;
import org.wit.hrmatching.dto.support.JobPostDTO;
import org.wit.hrmatching.dto.support.SupportPostDetailDTO;
import org.wit.hrmatching.dto.support.UserDetailDTO;
import org.wit.hrmatching.service.support.ReportService;
import org.wit.hrmatching.vo.support.ReportVO;
import org.wit.hrmatching.vo.user.CustomUserDetails;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminReportRestController {

    private final ReportService reportService;

    // 신고 목록
    @GetMapping
    public PagedResponseDTO<ReportVO> getReportList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String reportType,
            @RequestParam(required = false) String status
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ReportVO> reportPage = reportService.getPagedReports(pageable, reportType, status);

        return PagedResponseDTO.<ReportVO>builder()
                .content(reportPage.getContent())
                .totalElements(reportPage.getTotalElements())
                .totalPages(reportPage.getTotalPages())
                .size(reportPage.getSize())
                .number(reportPage.getNumber() + 1)
                .first(reportPage.isFirst())
                .last(reportPage.isLast())
                .numberOfElements(reportPage.getNumberOfElements())
                .empty(reportPage.isEmpty())
                .build();
    }

    // 신고 통계
    @GetMapping("/stats")
    public Map<String, Object> getReportStats() {
        return reportService.getReportStats();
    }


    // 신고 기각 처리
    @PostMapping("/{reportId}/dismiss")
    public void dismissReport(@PathVariable Long reportId,
                              @AuthenticationPrincipal CustomUserDetails admin) {
        reportService.dismissReport(reportId, admin.getUser().getId());
    }

    // 신고 제재 적용 처리
    @PostMapping("/{reportId}/apply")
    public void applyReportAction(@PathVariable Long reportId,
                                  @RequestBody Map<String, String> payload,
                                  @AuthenticationPrincipal CustomUserDetails admin) {
        String action = payload.get("action"); // "WARNING_1", "WARNING_2", "SUSPEND"
        reportService.applyReportAction(reportId, action, admin.getUser().getId());
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportVO> getReportDetail(@PathVariable Long reportId) {
        ReportVO report = reportService.getReportById(reportId);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }


    @GetMapping("/contents/posts/{postId}")
    public JobPostDTO getReportedJobPost(@PathVariable Long postId) {
        return reportService.getJobPostById(postId); // 비공개 공고라도 관리자 조회 허용
    }

    @GetMapping("/contents/community-posts/{postId}")
    public SupportPostDetailDTO getReportedCommunityPost(@PathVariable Long postId) {
        return reportService.getPostDetailById(postId);
    }

    @GetMapping("/contents/comments/{commentId}")
    public ResponseEntity<CommentDTO> getReportedComment(@PathVariable Long commentId) {
        CommentDTO comment = reportService.getCommentById(commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comment);
    }
}
