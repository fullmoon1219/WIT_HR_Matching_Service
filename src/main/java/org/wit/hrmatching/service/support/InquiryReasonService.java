package org.wit.hrmatching.service.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.support.InquiryReasonMapper;
import org.wit.hrmatching.vo.InquiryReasonVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryReasonService {

    private final InquiryReasonMapper reasonMapper;

    public List<InquiryReasonVO> getAllReasons() {
        return reasonMapper.selectAll();
    }
}

