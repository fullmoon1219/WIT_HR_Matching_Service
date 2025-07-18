package org.wit.hrmatching.dao.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.dto.applicant.ProfileDTO;
import org.wit.hrmatching.dto.applicant.ProfileUpdateRequestDTO;
import org.wit.hrmatching.mapper.applicant.ApplicantProfileMapper;

@Repository
@RequiredArgsConstructor
public class ApplicantProfileDAO {

	private final ApplicantProfileMapper applicantProfileMapper;

	public ProfileDTO getUserProfile(long userId) {
		return applicantProfileMapper.selectUserProfile(userId);
	}

	public void updateUserProfile(ProfileUpdateRequestDTO profileUpdateRequestDTO) {
		applicantProfileMapper.updateUserProfile(profileUpdateRequestDTO);
	}

	public String findPasswordById(long userId) {
		return applicantProfileMapper.findPasswordById(userId);
	}
}
