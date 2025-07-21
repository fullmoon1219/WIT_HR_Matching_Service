package org.wit.hrmatching.service.applicant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wit.hrmatching.dao.applicant.ResumeDAO;
import org.wit.hrmatching.exception.IncompleteProfileException;
import org.wit.hrmatching.exception.ResumeNotFoundException;
import org.wit.hrmatching.vo.user.ApplicantProfilesVO;
import org.wit.hrmatching.vo.resume.ResumeVO;
import org.wit.hrmatching.vo.user.UserVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeService {

	private final ResumeDAO resumeDAO;

	public Map<String, Object> getUserProfile(long userId) {

		UserVO userInfo = resumeDAO.getUserInfo(userId);
		ApplicantProfilesVO profile = resumeDAO.getApplicantProfile(userId);

		Map<String, Object> userProfile = new HashMap<>();
		userProfile.put("userInfo", userInfo);
		userProfile.put("profile", profile);

		return userProfile;
	}

	public boolean registerResume(ResumeVO resumeVO) {
		return resumeDAO.registerResume(resumeVO);
	}

	public List<ResumeVO> getCompletedResumeList(Long userId) {

		List<ResumeVO> completedList = new ArrayList<>();

		ResumeVO publicResume = resumeDAO.getPublicResume(userId);

		if (publicResume != null) {
			completedList.add(publicResume);
		}

		List<ResumeVO> otherResumes = resumeDAO.getResumeList(userId);

		for (ResumeVO resume : otherResumes) {
			completedList.add(resume);
		}

		return completedList;
	}


	public List<ResumeVO> getDraftResumeList(long userId) {
		return resumeDAO.getDraftResumeList(userId);
	}

	public boolean setResumeAsPrivate(long resumeId, long userId) {
		resumeDAO.clearPrimaryResume(userId);
		return resumeDAO.updatePrivateResume(resumeId);
	}

	@Transactional
	public void setResumeAsPublic(long resumeId, long userId) {

		if (!confirmProfile(userId)) {
			throw new IncompleteProfileException("개인정보 입력이 완료되지 않았습니다.");
		}

		resumeDAO.resetPublicResume(userId);
		resumeDAO.updatePrimaryResume(resumeId, userId);
		resumeDAO.updatePublicResume(resumeId);
	}

	public boolean confirmProfile(long userId) {

		ApplicantProfilesVO profile = resumeDAO.getApplicantProfile(userId);

		if (profile == null) return false;

		if (profile.getAddress() == null || profile.getAddress().trim().isEmpty()) return false;
		if (profile.getPhoneNumber() == null || profile.getPhoneNumber().trim().isEmpty()) return false;
		if (profile.getPortfolioUrl() == null || profile.getPortfolioUrl().trim().isEmpty()) return false;
		if (profile.getSelfIntro() == null || profile.getSelfIntro().trim().isEmpty()) return false;

		if (profile.getAge() == null || profile.getAge() <= 0) return false;

		if (profile.getExperienceYears() == null) return false;

		if (profile.getGender() == null) return false;
		if (profile.getJobType() == null) return false;

		return true;
	}

	public ResumeVO getResume(long resumeId) {
		return resumeDAO.getResume(resumeId);
	}

	public Long findOwnerIdByResumeId(long resumeId) {
		return resumeDAO.findOwnerIdByResumeId(resumeId);
	}

	@Transactional
	public void editResume(ResumeVO resumeVO) {

		ResumeVO existingResume = resumeDAO.getResume(resumeVO.getId());

		if (existingResume == null) {
			throw new ResumeNotFoundException("수정할 이력서를 찾을 수 없습니다.");
		}

		boolean updateSuccess = resumeDAO.editResume(resumeVO);

		if (!updateSuccess) {
			throw new RuntimeException("이력서 수정 중 데이터베이스 오류가 발생했습니다.");
		}
	}

	public boolean deleteResume(long resumeId) {
		return resumeDAO.deleteResume(resumeId);
	}

	public JsonNode getResumeJsonById(Long resumeId) {
		ResumeVO resume = getResume(resumeId);
		if (resume == null) {
			throw new ResumeNotFoundException("해당 이력서를 찾을 수 없습니다.");
		}

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode content = mapper.createObjectNode();

		content.put("education", resume.getEducation());
		content.put("experience", resume.getExperience());
		content.put("skills", resume.getSkills());
		content.put("preferredLocation", resume.getPreferredLocation());
		content.put("salaryExpectation", resume.getSalaryExpectation());
		content.put("desiredPosition", resume.getDesiredPosition());
		content.put("coreCompetency", resume.getCoreCompetency());
		content.put("motivation", resume.getMotivation());

		ObjectNode root = mapper.createObjectNode();
		root.set("content", mapper.createArrayNode().add(content));

		return root;
	}

}
