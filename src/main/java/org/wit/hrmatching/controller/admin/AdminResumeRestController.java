package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
//import org.wit.hrmatching.service.ResumeService;
import org.wit.hrmatching.service.admin.AdminResumeService;
import org.wit.hrmatching.vo.ResumeVO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/resumes")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminResumeRestController {

    private final AdminResumeService adminResumeService;

    @GetMapping("/list")
    public PagedResponseDTO<ResumeVO> getResumes(@RequestParam(required = false) Long id,
                                                 @RequestParam(defaultValue = "false") boolean includeDeleted,
                                                 Pageable pageable) {

        Page<ResumeVO> page = adminResumeService.getPagedResumes(pageable, id, includeDeleted);

        return PagedResponseDTO.<ResumeVO>builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .size(page.getSize())
                .number(page.getNumber())
                .first(page.isFirst())
                .last(page.isLast())
                .numberOfElements(page.getNumberOfElements())
                .empty(page.isEmpty())
                .build();

    }
}
