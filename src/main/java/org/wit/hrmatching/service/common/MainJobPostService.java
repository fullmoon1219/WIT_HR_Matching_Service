package org.wit.hrmatching.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
import org.wit.hrmatching.mapper.common.MainJobPostMapper;
import org.wit.hrmatching.vo.job.JobPostVO;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainJobPostService {

    private final MainJobPostMapper jobPostMapper;

    public PagedResponseDTO<JobPostVO> getRandomJobs(int page, int count) {
        LocalDate today = LocalDate.now();
        int offset = page * count;

        List<JobPostVO> content = jobPostMapper.selectRandomJobs(today, count, offset);
        Long total = jobPostMapper.countActiveJobs(today);

        return buildPagedResponse(content, total, page, count);
    }


    public PagedResponseDTO<JobPostVO> getUrgentJobs(int page, int size, int days) {
        LocalDate today = LocalDate.now();
        LocalDate until = today.plusDays(days);
        int offset = page * size;

        List<JobPostVO> content = jobPostMapper.selectUrgentJobs(today, until, size, offset);
        Long total = jobPostMapper.countUrgentJobs(today, until);

        return buildPagedResponse(content, total, page, size);
    }


    private <T> PagedResponseDTO<T> buildPagedResponse(List<T> content, long total, int page, int size) {
        int totalPages = (int) Math.ceil((double) total / size);
        return PagedResponseDTO.<T>builder()
                .content(content)
                .totalElements(total)
                .totalPages(totalPages)
                .size(size)
                .number(page)
                .first(page == 0)
                .last(page >= totalPages - 1)
                .numberOfElements(content.size())
                .empty(content.isEmpty())
                .build();
    }
}
