package org.wit.hrmatching.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.admin.AdminUserMapper;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserMapper adminUserMapper;

    public void updateUserRole(Long userId, String role) {
        adminUserMapper.updateRole(userId, role);
    }


    public void updateUserStatus(Long userId, String status) {
        adminUserMapper.updateStatus(userId, status);

        if ("ACTIVE".equals(status)) {
            adminUserMapper.updateWarningCount(userId, 0);
        }
    }

    public void updateUserWarning(Long userId, int count) {
        adminUserMapper.updateWarningCount(userId, count);

        if (count >= 3) {
            adminUserMapper.updateStatus(userId, "SUSPENDED");
        }
    }
}
