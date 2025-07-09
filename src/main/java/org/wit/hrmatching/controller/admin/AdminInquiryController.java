package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.service.support.InquiryService;
import org.wit.hrmatching.vo.InquiryVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/inquiries")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final InquiryService inquiryService;

    // 통계 API
    @GetMapping("/stats")
    public Map<String, Integer> getStats() {
        Map<String, Integer> map = new HashMap<>();
        map.put("totalCount", inquiryService.getTotalCount());
        map.put("unansweredCount", inquiryService.getCountByStatus("WAITING"));
        map.put("answeredCount", inquiryService.getCountByStatus("ANSWERED"));
        return map;
    }

    // 목록 API
    @GetMapping
    public Map<String, Object> getInquiries(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String status,
                                            @RequestParam(required = false) String keyword) {

        List<InquiryVO> list = inquiryService.getPagedInquiries(status, keyword, page, size);
        int totalCount = inquiryService.getTotalFilteredCount(status, keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("content", list);
        result.put("totalPages", (int) Math.ceil((double) totalCount / size));
        result.put("number", page);
        return result;
    }

    // 상태 변경 API
    @PatchMapping("/status")
    public void updateStatus(@RequestBody Map<String, Object> body) {
        List<Integer> idList = (List<Integer>) body.get("inquiryIds");
        List<Long> ids = idList.stream().map(Long::valueOf).toList();
        String status = (String) body.get("status");
        inquiryService.updateStatus(ids, status);
    }

    // 일괄 삭제 API
    @DeleteMapping
    public void deleteInquiries(@RequestBody List<Long> ids) {
        inquiryService.deleteInquiries(ids);
    }

    @PatchMapping("/{id}/reply")
    public void updateReply(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reply = body.get("reply");
        if (reply == null || reply.trim().isEmpty()) {
            throw new IllegalArgumentException("답변 내용이 비어 있습니다.");
        }
        inquiryService.updateReply(id, reply);
    }

    @DeleteMapping("/{id}/reply")
    public void deleteReply(@PathVariable Long id) {
        inquiryService.deleteReply(id);
    }

}
