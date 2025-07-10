package org.wit.hrmatching.service.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.support.InquiriesMapper;
import org.wit.hrmatching.service.mail.MailService;
import org.wit.hrmatching.vo.InquiryVO;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiriesMapper inquiriesMapper;
    private final MailService mailService;

    public int getTotalCount() {
        return inquiriesMapper.countAll();
    }

    public int getCountByStatus(String status) {
        return inquiriesMapper.countByStatus(status);
    }

    public List<Map<String, Object>> getInquiryCountByReason() {
        return inquiriesMapper.countByReason();
    }

    public List<InquiryVO> getPagedInquiries(String status, String keyword, Long reasonId, int page, int size) {
        int offset = (page - 1) * size;
        return inquiriesMapper.selectAll(status, keyword, reasonId, offset, size);
    }

    public int getTotalFilteredCount(String status, String keyword, Long reasonId) {
        return inquiriesMapper.selectTotalCount(status, keyword, reasonId);
    }

    public void updateStatus(List<Long> ids, String status) {
        inquiriesMapper.updateStatus(ids, status);
    }

    public void deleteInquiries(List<Long> ids) {
        inquiriesMapper.deleteInquiries(ids);
    }

    public void updateReply(Long inquiryId, String reply) {
        InquiryVO inquiry = inquiriesMapper.selectById(inquiryId);
        if (inquiry == null) {
            throw new IllegalArgumentException("해당 문의를 찾을 수 없습니다.");
        }

        inquiry.setReply(reply);
        inquiry.setStatus("ANSWERED");
        inquiriesMapper.updateReplyAndStatus(inquiry);

        // 문의 답변 등록 메일 발송
//        mailService.sendInquiryAnswerNotification(
//                inquiry.getEmail(),
//                inquiry.getName(),
//                inquiryId
//        );
    }


    public void deleteReply(Long id) {
        inquiriesMapper.deleteReply(id);
    }



}
