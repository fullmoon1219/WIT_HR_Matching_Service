package org.wit.hrmatching.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.admin.AdminResumeMapper;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminResumeService {

    private final AdminResumeMapper adminResumeMapper;

    public Page<ResumeVO> getPagedResumes(Pageable pageable, Long userId, boolean includeDeleted) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        int total = adminResumeMapper.getTotalResumeCount(userId, includeDeleted);
        List<ResumeVO> resumes = adminResumeMapper.getPagedResumes(userId, includeDeleted, limit, offset);

        return new PageImpl<>(resumes, pageable, total);
    }

}
