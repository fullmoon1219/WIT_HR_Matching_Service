package org.wit.hrmatching.service.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.support.InquiriesMapper;
import org.wit.hrmatching.vo.InquiryVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiriesMapper inquiriesMapper;

    public int getTotalCount() {
        return inquiriesMapper.countAll();
    }

    public int getCountByStatus(String status) {
        return inquiriesMapper.countByStatus(status);
    }

    public List<InquiryVO> getPagedInquiries(String status, String keyword, int page, int size) {
        int offset = (page - 1) * size;
        return inquiriesMapper.selectAll(status, keyword, offset, size);
    }

    public int getTotalFilteredCount(String status, String keyword) {
        return inquiriesMapper.selectTotalCount(status, keyword);
    }

    public void updateStatus(List<Long> ids, String status) {
        inquiriesMapper.updateStatus(ids, status);
    }

    public void deleteInquiries(List<Long> ids) {
        inquiriesMapper.deleteInquiries(ids);
    }

    public void updateReply(Long inquiryId, String reply) {
        inquiriesMapper.updateReply(inquiryId, reply);
    }

    public void deleteReply(Long id) {
        inquiriesMapper.deleteReply(id);
    }

}
