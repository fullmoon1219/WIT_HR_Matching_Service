package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wit.hrmatching.service.admin.AdminUserService;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminUserRestController {

    private final AdminUserService adminUserService;

    @PostMapping("/change-value")
    public ResponseEntity<?> changeUserValue(
            @RequestParam Long userId,
            @RequestParam String type,
            @RequestParam String value) {

        try {
            switch (type) {
                case "role" -> adminUserService.updateUserRole(userId, value);
                case "status" -> adminUserService.updateUserStatus(userId, value);
                case "warning" -> {
                    int count = Integer.parseInt(value);
                    adminUserService.updateUserWarning(userId, count);
                }
                default -> {
                    return ResponseEntity.badRequest().body("지원하지 않는 변경 항목입니다.");
                }
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("변경 실패: " + e.getMessage());
        }
    }
}
