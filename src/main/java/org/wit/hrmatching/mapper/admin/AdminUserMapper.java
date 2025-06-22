package org.wit.hrmatching.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminUserMapper {
    void updateRole(@Param("userId") Long userId, @Param("role") String role);
    void updateStatus(@Param("userId") Long userId, @Param("status") String status);
    void updateWarningCount(@Param("userId") Long userId, @Param("count") int count);
}
