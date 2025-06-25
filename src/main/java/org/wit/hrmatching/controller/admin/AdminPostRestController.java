package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
import org.wit.hrmatching.service.admin.AdminPostService;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/posts")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminPostRestController {

    private final AdminPostService adminPostService;

    @GetMapping("/stats")
    public Map<String, Integer> getPostStats() {
        return adminPostService.getPostStats();
    }

    @GetMapping("/list")
    public PagedResponseDTO<JobPostVO> getPosts(@RequestParam(required = false) Long id,
                                                @RequestParam(required = false) Boolean isClosed,
                                                @RequestParam(required = false) Boolean isDeleted,
                                                @RequestParam(required = false) String keyword,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<JobPostVO> pageResult = adminPostService.getPagedPosts(pageable, id, isClosed, isDeleted, keyword);

        return PagedResponseDTO.<JobPostVO>builder()
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

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteJobPosts(@RequestBody List<Long> ids) {
        adminPostService.deletePostsByIds(ids);
        return ResponseEntity.ok().build();
    }

}
