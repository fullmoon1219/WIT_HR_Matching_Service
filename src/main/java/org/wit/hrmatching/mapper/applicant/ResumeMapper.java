package org.wit.hrmatching.mapper.applicant;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.wit.hrmatching.vo.user.ApplicantProfilesVO;
import org.wit.hrmatching.vo.resume.ResumeVO;
import org.wit.hrmatching.vo.user.UserVO;

import java.util.List;

@Mapper
public interface ResumeMapper {

	UserVO selectUserInfo(long userId);
	int insertResume(ResumeVO resumeVO);

	ResumeVO selectPublicResume(long userId);
	List<ResumeVO> selectResumeList(long userId);
	List<ResumeVO> selectDraftResumeList(long userId);

	int updatePrivateResume(long resumeId);
	int updatePublicResume(long resumeId);
	int resetPublicResume(long userId);

	ApplicantProfilesVO selectApplicantProfile(long userId);

	ResumeVO selectResume(long resumeId);

	@Select("SELECT user_id FROM resumes WHERE id = #{resumeId} and deleted_at is null")
	Long findOwnerIdByResumeId(Long resumeId);

	ResumeVO selectResumeForUpdate(long resumeId);
	int updateResume(ResumeVO resumeVO);

	int deleteResume(long resumeId);

	void updatePrimaryResume(long resumeId, long userId);
	void clearPrimaryResume(long userId);

	int countCompletedResumes(long userId);
}
