package org.wit.hrmatching.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.admin.AdminPostMapper;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.List;

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
}
