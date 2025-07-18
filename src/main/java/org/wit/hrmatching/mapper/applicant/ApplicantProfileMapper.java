package org.wit.hrmatching.mapper.applicant;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.wit.hrmatching.dto.applicant.ApplicantProfileDTO;
import org.wit.hrmatching.dto.applicant.ApplicantProfileUpdateRequestDTO;

@Mapper
public interface ApplicantProfileMapper {

	ApplicantProfileDTO selectUserProfile(long userId);

	int updateUserProfile(ApplicantProfileUpdateRequestDTO applicantProfileUpdateRequestDTO);

	@Select("SELECT password FROM users WHERE id = #{userId}")
	String findPasswordById(@Param("userId") long userId);
}
