package org.wit.hrmatching.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.admin.AdminResumeMapper;
import org.wit.hrmatching.vo.resume.ResumeVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminResumeService {

    private final AdminResumeMapper adminResumeMapper;

    // 이력서 리스트
    public Page<ResumeVO> getPagedResumes(Pageable pageable,
                                          Long id,
                                          Boolean isPublic,
                                          Boolean isCompleted,
                                          Boolean includeDeleted,
                                          String keyword) {

        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();

        List<ResumeVO> resumes = adminResumeMapper.getPagedResumes(id, isPublic, isCompleted, includeDeleted, keyword, limit, offset);
        int total = adminResumeMapper.countResumes(id, isPublic, isCompleted, includeDeleted, keyword);

        return new PageImpl<>(resumes, pageable, total);
    }

    // 이력서 통계
    public Map<String, Integer> getResumeStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", adminResumeMapper.countAllResumes());
        stats.put("completed", adminResumeMapper.countCompletedResumes());
        stats.put("incompleted", adminResumeMapper.countIncompletedResumes());
        stats.put("public", adminResumeMapper.countPublicResumes());
        stats.put("deleted", adminResumeMapper.countDeletedResumes());
        return stats;
    }

    // 이력서 삭제
    public void deleteResumes(List<Long> resumeIds) {
        if (resumeIds == null || resumeIds.isEmpty()) return;
        adminResumeMapper.deleteResumesByIds(resumeIds);
    }


}
