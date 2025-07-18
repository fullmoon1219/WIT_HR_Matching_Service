package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wit.hrmatching.config.file.FileUploadProperties;
import org.wit.hrmatching.dao.applicant.ApplicantProfileDAO;
import org.wit.hrmatching.dto.applicant.ApplicantProfileDTO;
import org.wit.hrmatching.dto.applicant.ApplicantProfileUpdateRequestDTO;

@Service
@RequiredArgsConstructor
public class ApplicantProfileService {

	private final ApplicantProfileDAO applicantProfileDAO;
	private final PasswordEncoder passwordEncoder;
	private final FileUploadProperties fileUploadProperties;

	public ApplicantProfileDTO getUserProfile(long userId) {
		return applicantProfileDAO.getUserProfile(userId);
	}

	@Transactional
	public void updateUserProfile(long userId, ApplicantProfileUpdateRequestDTO dto) {

		String encodedPasswordFromDB = applicantProfileDAO.findPasswordById(userId);

		boolean isMatch = passwordEncoder.matches(dto.getPassword(), encodedPasswordFromDB);

		if (!isMatch) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}

		dto.setUserId(userId);
		applicantProfileDAO.updateUserProfile(dto);
	}

}
