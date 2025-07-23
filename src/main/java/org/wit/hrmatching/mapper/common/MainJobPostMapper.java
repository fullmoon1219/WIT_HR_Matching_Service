package org.wit.hrmatching.mapper.common;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.wit.hrmatching.vo.job.JobPostVO;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MainJobPostMapper {

    // 랜덤으로 공고 3개 가져오기
    List<JobPostVO> selectRandomJobs(@Param("now") LocalDate now,
                                     @Param("limit") int limit,
                                     @Param("offset") int offset);


    // 마감 임박 공고 가져오기
    List<JobPostVO> selectUrgentJobs(@Param("start") LocalDate start,
                                     @Param("end") LocalDate end,
                                     @Param("limit") int limit,
                                     @Param("offset") int offset);

    // 활성화된 공고 총 개수
    Long countActiveJobs(@Param("now") LocalDate now);

    // 마감 임박 공고 총 개수
    Long countUrgentJobs(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
