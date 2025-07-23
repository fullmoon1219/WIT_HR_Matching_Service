package org.wit.hrmatching.controller.common;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
import org.wit.hrmatching.service.common.MainJobPostService;
import org.wit.hrmatching.vo.job.JobPostVO;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobPostRestController {

    private final MainJobPostService jobPostService;

    // 랜덤 3개 공고 가져오기
    @GetMapping("/random")
    public PagedResponseDTO<JobPostVO> getRandomJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int count

    ) {
        return jobPostService.getRandomJobs(page, count);
    }

    // 마감 임박 공고 가져오기
    @GetMapping("/urgent")
    public PagedResponseDTO<JobPostVO> getUrgentJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "5") int days

    ) {
        return jobPostService.getUrgentJobs(page, size, days);
    }
}

