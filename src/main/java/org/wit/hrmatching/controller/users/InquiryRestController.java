package org.wit.hrmatching.controller.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
import org.wit.hrmatching.service.support.InquiryReasonService;
import org.wit.hrmatching.service.support.InquiryService;
import org.wit.hrmatching.vo.support.InquiryReasonVO;
import org.wit.hrmatching.vo.support.InquiryVO;
import org.wit.hrmatching.vo.user.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/users/inquiries")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class InquiryRestController {

    private final InquiryReasonService reasonService;
    private final InquiryService inquiryService;

    @GetMapping("/reasons")
    public List<InquiryReasonVO> getAll() {
        return reasonService.getAllReasons();
    }


    @PostMapping
    public void registerInquiry(@RequestBody InquiryVO inquiryVO,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            inquiryVO.setUserId(userDetails.getId());
        }
        inquiryService.registerInquiry(inquiryVO);
    }

    @GetMapping
    public PagedResponseDTO<InquiryVO> getUserInquiries(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return inquiryService.getPagedInquiriesByUserId(userDetails.getId(), page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InquiryVO> getInquiryDetail(@PathVariable Long id) {
        InquiryVO inquiry = inquiryService.getInquiryById(id);
        if (inquiry == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inquiry);
    }
}
