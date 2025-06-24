package org.wit.hrmatching.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminUserMapper {
    void updateRole(@Param("userId") Long userId, @Param("role") String role);
    void updateStatus(@Param("userId") Long userId, @Param("status") String status);
    void updateWarningCount(@Param("userId") Long userId, @Param("count") int count);

    void deleteApplicantProfilesIfExists(@Param("userIds") List<Long> userIds);
    void deleteEmployerProfilesIfExists(@Param("userIds") List<Long> userIds);
    void deleteUsers(@Param("userIds") List<Long> userIds);
}
