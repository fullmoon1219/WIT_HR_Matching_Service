package org.wit.hrmatching.dao.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.dto.applicant.ProfileDTO;
import org.wit.hrmatching.dto.applicant.ProfileUpdateRequestDTO;
import org.wit.hrmatching.mapper.applicant.ProfileMapper;

@Repository
@RequiredArgsConstructor
public class ProfileDAO {

	private final ProfileMapper profileMapper;

	public ProfileDTO getUserProfile(long userId) {
		return profileMapper.selectUserProfile(userId);
	}

	public void updateUserProfile(ProfileUpdateRequestDTO profileUpdateRequestDTO) {
		profileMapper.updateUserProfile(profileUpdateRequestDTO);
	}

	public String findPasswordById(long userId) {
		return profileMapper.findPasswordById(userId);
	}
}
