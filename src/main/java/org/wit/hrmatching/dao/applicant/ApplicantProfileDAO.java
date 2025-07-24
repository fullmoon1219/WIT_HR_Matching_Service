package org.wit.hrmatching.dao.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.dto.applicant.ApplicantProfileDTO;
import org.wit.hrmatching.dto.applicant.ApplicantProfileUpdateRequestDTO;
import org.wit.hrmatching.mapper.applicant.ApplicantProfileMapper;
import org.wit.hrmatching.vo.user.ApplicantProfileImageVO;

@Repository
@RequiredArgsConstructor
public class ApplicantProfileDAO {

	private final ApplicantProfileMapper applicantProfileMapper;

	public ApplicantProfileDTO getUserProfile(long userId) {
		return applicantProfileMapper.selectUserProfile(userId);
	}

	public void updateUser(ApplicantProfileUpdateRequestDTO applicantProfileUpdateRequestDTO) {
		applicantProfileMapper.updateUser(applicantProfileUpdateRequestDTO);
	}

	public void updateApplicantProfile(ApplicantProfileUpdateRequestDTO applicantProfileUpdateRequestDTO) {
		applicantProfileMapper.updateApplicantProfile(applicantProfileUpdateRequestDTO);
	}

	public String findPasswordById(long userId) {
		return applicantProfileMapper.findPasswordById(userId);
	}

	public void insertFile(ApplicantProfileImageVO vo) {
		applicantProfileMapper.insertFile(vo);
	}

	public void updateUserProfileImage(String profileImage, long userId) {
		applicantProfileMapper.updateUserProfileImage(profileImage, userId);
	}

	public String findProfileImageByUserId(long userId) {
		return applicantProfileMapper.findProfileImageByUserId(userId);
	}

	public void updatePassword(long userId, String newPassword) {
		applicantProfileMapper.updatePassword(userId, newPassword);
	}
}
