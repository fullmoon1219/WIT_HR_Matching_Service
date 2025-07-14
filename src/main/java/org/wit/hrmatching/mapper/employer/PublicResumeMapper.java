package org.wit.hrmatching.mapper.employer;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.resume.PublicResumeVO;

import java.util.List;

@Mapper
public interface PublicResumeMapper {

	List<PublicResumeVO> searchApplicants(List<String> locations, List<Long> skills, String keyword);

	PublicResumeVO selectResumeById(Long resumeId);
}
