package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
import org.wit.hrmatching.service.admin.AdminResumeService;
import org.wit.hrmatching.vo.resume.ResumeVO;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/resumes")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminResumeRestController {

    private final AdminResumeService adminResumeService;

    // ✅ 통계 조회: /api/admin/resumes/statistics
    @GetMapping("/statistics")
    public Map<String, Integer> getResumesStatistics() {
        return adminResumeService.getResumeStats();
    }

    // ✅ 목록 조회: /api/admin/resumes?page=1&size=10...
    @GetMapping
    public PagedResponseDTO<ResumeVO> getResumes(@RequestParam(required = false) Long id,
                                                 @RequestParam(required = false) Boolean isPublic,
                                                 @RequestParam(required = false) Boolean isCompleted,
                                                 @RequestParam(required = false) Boolean includeDeleted,
                                                 @RequestParam(required = false) String keyword,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<ResumeVO> pageResult = adminResumeService.getPagedResumes(pageable, id, isPublic, isCompleted, includeDeleted, keyword);

        return PagedResponseDTO.<ResumeVO>builder()
                .content(pageResult.getContent())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .size(pageResult.getSize())
                .number(pageResult.getNumber())
                .first(pageResult.isFirst())
                .last(pageResult.isLast())
                .numberOfElements(pageResult.getNumberOfElements())
                .empty(pageResult.isEmpty())
                .build();
    }

    // ✅ 삭제: DELETE /api/admin/resumes (body에 resumeIds 전달)
    @DeleteMapping
    public ResponseEntity<?> deleteResumes(@RequestBody List<Long> resumeIds) {
        adminResumeService.deleteResumes(resumeIds);
        return ResponseEntity.ok().build();
    }
}
