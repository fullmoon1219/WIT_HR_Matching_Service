package org.wit.hrmatching.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.wit.hrmatching.vo.resume.ResumeVO;

import java.util.List;

@Mapper
public interface AdminResumeMapper {

    // 필터 기반 이력서 리스트
    List<ResumeVO> getPagedResumes(@Param("id") Long id,
                                   @Param("isPublic") Boolean isPublic,
                                   @Param("isCompleted") Boolean isCompleted,
                                   @Param("includeDeleted") Boolean includeDeleted,
                                   @Param("keyword") String keyword,
                                   @Param("limit") int limit,
                                   @Param("offset") int offset);

    // 필터 기반 이력서 수
    int countResumes(@Param("id") Long id,
                     @Param("isPublic") Boolean isPublic,
                     @Param("isCompleted") Boolean isCompleted,
                     @Param("includeDeleted") Boolean includeDeleted,
                     @Param("keyword") String keyword);


    // 전체 이력서 수
    @Select("SELECT COUNT(*) FROM resumes")
    int countAllResumes();

    // 완료된 이력서 수
    @Select("SELECT COUNT(*) FROM resumes WHERE is_completed = TRUE")
    int countCompletedResumes();

    // 미완료 이력서 수
    @Select("SELECT COUNT(*) FROM resumes WHERE is_completed = FALSE")
    int countIncompletedResumes();

    // 공개 이력서 수
    @Select("SELECT COUNT(*) FROM resumes WHERE is_public = TRUE")
    int countPublicResumes();

    // 삭제된 이력서 수
    @Select("SELECT COUNT(*) FROM resumes WHERE deleted_at IS NOT NULL")
    int countDeletedResumes();

    int deleteResumesByIds(@Param("resumeIds") List<Long> resumeIds);


}
