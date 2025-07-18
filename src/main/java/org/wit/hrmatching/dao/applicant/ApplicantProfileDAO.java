package org.wit.hrmatching.dao.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.dto.applicant.ApplicantProfileDTO;
import org.wit.hrmatching.dto.applicant.ApplicantProfileUpdateRequestDTO;
import org.wit.hrmatching.mapper.applicant.ApplicantProfileMapper;

@Repository
@RequiredArgsConstructor
public class ApplicantProfileDAO {

	private final ApplicantProfileMapper applicantProfileMapper;

	public ApplicantProfileDTO getUserProfile(long userId) {
		return applicantProfileMapper.selectUserProfile(userId);
	}

	public void updateUserProfile(ApplicantProfileUpdateRequestDTO applicantProfileUpdateRequestDTO) {
		applicantProfileMapper.updateUserProfile(applicantProfileUpdateRequestDTO);
	}

	public String findPasswordById(long userId) {
		return applicantProfileMapper.findPasswordById(userId);
	}
}
