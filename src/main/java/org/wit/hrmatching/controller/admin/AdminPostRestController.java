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

    /**
     * 공고 통계 정보 반환
     */
    @GetMapping("/stats")
    public Map<String, Integer> getPostStats() {
        return adminPostService.getPostStats();
    }

    /**
     * 공고 목록 조회
     */
    @GetMapping
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
                .number(pageResult.getNumber() + 1) // 클라이언트는 1부터 시작
                .first(pageResult.isFirst())
                .last(pageResult.isLast())
                .numberOfElements(pageResult.getNumberOfElements())
                .empty(pageResult.isEmpty())
                .build();
    }

    /**
     * 공고 삭제 (복수)
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteJobPosts(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        adminPostService.deletePostsByIds(ids);
        return ResponseEntity.ok().build();
    }
}
