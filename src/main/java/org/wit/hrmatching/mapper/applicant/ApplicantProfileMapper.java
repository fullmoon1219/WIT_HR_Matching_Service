package org.wit.hrmatching.mapper.applicant;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.wit.hrmatching.dto.applicant.ProfileDTO;
import org.wit.hrmatching.dto.applicant.ProfileUpdateRequestDTO;

@Mapper
public interface ApplicantProfileMapper {

	ProfileDTO selectUserProfile(long userId);

	int updateUserProfile(ProfileUpdateRequestDTO profileUpdateRequestDTO);

	@Select("SELECT password FROM users WHERE id = #{userId}")
	String findPasswordById(@Param("userId") long userId);
}
