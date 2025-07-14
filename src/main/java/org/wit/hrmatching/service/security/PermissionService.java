package org.wit.hrmatching.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.service.applicant.ApplicationService;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.service.applicant.ResumeService;

@Service("permission")
@RequiredArgsConstructor
public class PermissionService {

    private final ResumeService resumeService;
    private final ApplicationService applicationService;

    public boolean isResumeOwner(Long resumeId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long loginUserId = userDetails.getId();

        Long ownerId = resumeService.findOwnerIdByResumeId(resumeId);
        return loginUserId.equals(ownerId);
    }

    public boolean isApplicationOwner(Long applicationId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long loginUserId = userDetails.getId();

        Long ownerId = applicationService.findOwnerIdByApplicationId(applicationId);
        return ownerId != null && loginUserId.equals(ownerId);
    }
}
