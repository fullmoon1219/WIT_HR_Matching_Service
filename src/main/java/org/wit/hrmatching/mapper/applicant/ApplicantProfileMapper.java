package org.wit.hrmatching.mapper.applicant;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.wit.hrmatching.dto.applicant.ApplicantProfileDTO;
import org.wit.hrmatching.dto.applicant.ApplicantProfileUpdateRequestDTO;
import org.wit.hrmatching.vo.user.ApplicantProfileImageVO;

@Mapper
public interface ApplicantProfileMapper {

	ApplicantProfileDTO selectUserProfile(long userId);

	int updateUser(ApplicantProfileUpdateRequestDTO applicantProfileUpdateRequestDTO);
	int updateApplicantProfile(ApplicantProfileUpdateRequestDTO applicantProfileUpdateRequestDTO);

	@Select("SELECT password FROM users WHERE id = #{userId}")
	String findPasswordById(@Param("userId") long userId);

	int insertFile(ApplicantProfileImageVO applicantProfileImageVO);
	int updateUserProfileImage(@Param("profileImage") String profileImage, @Param("userId") long userId);
	String findProfileImageByUserId(long userId);

	int updatePassword(@Param("userId") long userId, @Param("newPassword") String newPassword);
}
