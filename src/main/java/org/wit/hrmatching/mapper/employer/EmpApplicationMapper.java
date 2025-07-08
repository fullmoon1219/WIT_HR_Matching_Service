package org.wit.hrmatching.mapper.employer;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.EmpApplicationVO;
import org.wit.hrmatching.vo.EmployerRecentApplicantVO;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpApplicationMapper {

    List<EmployerRecentApplicantVO> selectApplicantToEmployerList(@Param("userId") Long userId,
                                                                  @Param("keyword") String keyword,
                                                                  @Param("offset") int offset,
                                                                  @Param("size") int size);

    int countApplicantList(@Param("userId") Long userId,
                           @Param("keyword") String keyword);


    EmpApplicationVO selectResumeDetail(Long resumeId, Long jobPostId) ;

    int updateViewAt(Long applicationId);

    int updateStatus(Long applicationId, String status);

    List<Map<String, Object>> countApplicationsByStatus(Long userId);

    Long countUnviewedApplications(Long userId);
}
