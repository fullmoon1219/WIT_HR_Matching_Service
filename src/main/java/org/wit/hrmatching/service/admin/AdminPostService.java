package org.wit.hrmatching.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.admin.AdminPostMapper;
import org.wit.hrmatching.vo.job.JobPostVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final AdminPostMapper adminPostMapper;


    public Page<JobPostVO> getPagedPosts(Pageable pageable, Long id, Boolean status, Boolean deleted, String keyword) {

        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        List<JobPostVO> jobPosts = adminPostMapper.getFilteredJobPostsPaged(id, status, deleted, keyword, limit, offset);
        int total = adminPostMapper.countFilteredJobPosts(id, status, deleted, keyword);

        return new PageImpl<>(jobPosts, pageable, total);
    }

    public void deletePostsByIds(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            adminPostMapper.deletePostsByIds(ids);
        }
    }

    public Map<String, Integer> getPostStats() {
        Map<String, Integer> stats = new HashMap<>();

        stats.put("total", adminPostMapper.countAll());
        stats.put("available", adminPostMapper.countAvailable());
        stats.put("closed", adminPostMapper.countByStatus(true));
        stats.put("deleted", adminPostMapper.countDeleted());

        return stats;
    }

}
