package org.wit.hrmatching.controller.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.service.support.ReportService;
import org.wit.hrmatching.vo.support.ReportVO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.wit.hrmatching.vo.user.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/reports")
@PreAuthorize("isAuthenticated()")
public class ReportRestController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<?> submitReport(@RequestBody ReportVO report,
                                          @AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        report.setReporterUserId(user.getId());
        reportService.registerReport(report);
        return ResponseEntity.ok("신고가 등록되었습니다.");
    }
}