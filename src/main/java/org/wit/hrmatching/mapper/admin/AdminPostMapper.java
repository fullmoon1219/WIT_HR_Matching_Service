package org.wit.hrmatching.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.List;

@Mapper
public interface AdminPostMapper {

    List<JobPostVO> getFilteredJobPostsPaged(
            @Param("id") Long id,
            @Param("status") Boolean status,
            @Param("deleted") Boolean deleted,
            @Param("keyword") String keyword,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    int countFilteredJobPosts(
            @Param("id") Long id,
            @Param("status") Boolean status,
            @Param("deleted") Boolean deleted,
            @Param("keyword") String keyword
    );

    void deletePostsByIds(@Param("ids") List<Long> ids);

    // 상태
    int countAll();
    int countByStatus(boolean status);
    int countDeleted();
    int countAvailable();

}
