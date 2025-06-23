package org.wit.hrmatching.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;

@Mapper
public interface AdminResumeMapper {

    List<ResumeVO> getPagedResumes(@Param("userId") Long userId,
                                   @Param("includeDeleted") boolean includeDeleted,
                                   @Param("limit") int limit,
                                   @Param("offset") int offset);

    int getTotalResumeCount(@Param("userId") Long userId,
                            @Param("includeDeleted") boolean includeDeleted);
}
