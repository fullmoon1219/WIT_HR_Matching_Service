// AdminUserRestController.java
package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.admin.*;
import org.wit.hrmatching.dto.admin.updateRequest.RoleUpdateRequest;
import org.wit.hrmatching.dto.admin.updateRequest.StatusUpdateRequest;
import org.wit.hrmatching.dto.admin.updateRequest.WarningUpdateRequest;
import org.wit.hrmatching.service.admin.AdminUserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminUserRestController {

    private final AdminUserService adminUserService;

    @PatchMapping("/role")
    public ResponseEntity<?> updateUserRole(@RequestBody RoleUpdateRequest request) {
        if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
            return ResponseEntity.badRequest().body("변경 대상이 없습니다.");
        }
        adminUserService.updateUserRole(request.getUserIds(), request.getRole());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/status")
    public ResponseEntity<?> updateUserStatus(@RequestBody StatusUpdateRequest request) {
        if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
            return ResponseEntity.badRequest().body("변경 대상이 없습니다.");
        }
        adminUserService.updateUserStatus(request.getUserIds(), request.getStatus());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/warning")
    public ResponseEntity<?> updateUserWarning(@RequestBody WarningUpdateRequest request) {
        if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
            return ResponseEntity.badRequest().body("변경 대상이 없습니다.");
        }
        adminUserService.updateUserWarning(request.getUserIds(), request.getCount());
        return ResponseEntity.ok().build();
    }

    // ✅ 사용자 삭제 (복수)
    @DeleteMapping
    public ResponseEntity<?> deleteUsers(@RequestBody List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return ResponseEntity.badRequest().body("삭제 대상이 없습니다.");
        }
        adminUserService.deleteUsers(userIds);
        return ResponseEntity.ok().build();
    }
}
