package org.wit.hrmatching.mapper.employer;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.PublicResumeVO;
import org.wit.hrmatching.vo.ResumeVO;
import org.wit.hrmatching.vo.UserVO;

import java.util.List;

@Mapper
public interface PublicResumeMapper {

	List<PublicResumeVO> searchApplicants(List<String> locations, List<Long> skills, String keyword);

	PublicResumeVO selectResumeById(Long resumeId);
}
