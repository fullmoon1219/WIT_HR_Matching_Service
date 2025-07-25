package org.wit.hrmatching.service.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
import org.wit.hrmatching.mapper.support.InquiriesMapper;
import org.wit.hrmatching.service.mail.MailService;
import org.wit.hrmatching.vo.support.InquiryVO;

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

        mailService.sendInquiryAnswerNotification(
                inquiry.getEmail(),
                inquiry.getName(),
                inquiryId
        );
    }


    public void deleteReply(Long id) {
        inquiriesMapper.deleteReply(id);
    }


    public void registerInquiry(InquiryVO inquiryVO) {
        inquiriesMapper.insertInquiry(inquiryVO);
    }

    public PagedResponseDTO<InquiryVO> getPagedInquiriesByUserId(Long userId, int page, int size) {
        int offset = page * size;
        List<InquiryVO> content = inquiriesMapper.getInquiriesByUserId(userId, size, offset);
        long total = inquiriesMapper.countInquiriesByUserId(userId);
        int totalPages = (int) Math.ceil((double) total / size);

        return PagedResponseDTO.<InquiryVO>builder()
                .content(content)
                .totalElements(total)
                .totalPages(totalPages)
                .size(size)
                .number(page)
                .first(page == 0)
                .last(page >= totalPages - 1)
                .numberOfElements(content.size())
                .empty(content.isEmpty())
                .build();
    }

    public InquiryVO getInquiryById(Long id) {
        return inquiriesMapper.getInquiryById(id);
    }
}
