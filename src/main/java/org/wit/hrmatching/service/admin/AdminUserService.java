package org.wit.hrmatching.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wit.hrmatching.mapper.admin.AdminUserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserMapper adminUserMapper;

    public void updateUserRole(List<Long> userIds, String role) {
        for (Long userId : userIds) {
            adminUserMapper.updateRole(userId, role);
        }
    }


    public void updateUserStatus(List<Long> userIds, String status) {
        for (Long userId : userIds) {
            adminUserMapper.updateStatus(userId, status);

            if ("ACTIVE".equals(status)) {
                adminUserMapper.updateWarningCount(userId, 0);
            }
        }
    }

    public void updateUserWarning(List<Long> userIds, int count) {
        for (Long userId : userIds) {
            adminUserMapper.updateWarningCount(userId, count);

            if (count >= 3) {
                adminUserMapper.updateStatus(userId, "SUSPENDED");
            } else if (count == 0) {
                adminUserMapper.updateStatus(userId, "ACTIVE");
            }
        }
    }

    @Transactional
    public void deleteUsers(List<Long> userIds) {
        // 프로필 테이블에서 해당 유저 ID가 있는 경우 삭제 (존재 여부 판단 없이 실행 가능)
        adminUserMapper.deleteApplicantProfilesIfExists(userIds);
        adminUserMapper.deleteEmployerProfilesIfExists(userIds);

        // users 테이블에서 삭제
        adminUserMapper.deleteUsers(userIds);
    }
}
