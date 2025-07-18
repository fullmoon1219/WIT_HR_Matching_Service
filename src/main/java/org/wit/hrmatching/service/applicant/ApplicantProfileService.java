package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wit.hrmatching.config.file.FileUploadProperties;
import org.wit.hrmatching.dao.applicant.ApplicantProfileDAO;
import org.wit.hrmatching.dto.applicant.ApplicantProfileDTO;
import org.wit.hrmatching.dto.applicant.ApplicantProfileUpdateRequestDTO;
import org.wit.hrmatching.vo.user.ApplicantProfileImageVO;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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

	@Transactional
	public String updateProfileImage(long userId,  MultipartFile multipartFile) {

		String oldImagePath = applicantProfileDAO.findProfileImageByUserId(userId);

		// 파일 고유 이름 생성
		String originalFileName = multipartFile.getOriginalFilename();
		String fileExtension = "";
		if (originalFileName != null && originalFileName.contains(".")) {
			fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		}
		String storedFileName = UUID.randomUUID().toString() + fileExtension;

		// 저장 경로 설정
		String uploadPath = fileUploadProperties.getUserProfile();
		File directory = new File(uploadPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		String fullPath = uploadPath + storedFileName;

		try {
			multipartFile.transferTo(new File(fullPath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// DB에 파일 정보 저장
		ApplicantProfileImageVO fileVO = new ApplicantProfileImageVO();
		fileVO.setUserId(userId);
		fileVO.setRelatedId(userId);
		fileVO.setOriginalName(originalFileName);
		fileVO.setStoredName(storedFileName);
		fileVO.setFileSize(multipartFile.getSize());
		fileVO.setUploadPath(uploadPath);

		applicantProfileDAO.insertFile(fileVO);
		applicantProfileDAO.updateUserProfileImage(fullPath, userId);

		// 기존 파일 삭제
		if (oldImagePath != null && !oldImagePath.isEmpty()) {
			File oldFile = new File(oldImagePath);
			if (oldFile.exists()) {
				oldFile.delete();
			}
		}

		return fullPath;
	}
}
