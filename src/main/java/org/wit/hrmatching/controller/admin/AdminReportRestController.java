package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
import org.wit.hrmatching.service.support.ReportService;
import org.wit.hrmatching.vo.support.ReportVO;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminReportRestController {

    private final ReportService reportService;

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


    @GetMapping("/stats")
    public Map<String, Object> getReportStats() {
        return reportService.getReportStats();
    }
}
