package org.wit.hrmatching.service.support;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.support.ReportMapper;
import org.wit.hrmatching.vo.ReportVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportMapper reportMapper;

    public Page<ReportVO> getPagedReports(Pageable pageable, String reportType, String status) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("reportType", reportType);
        filter.put("status", status);
        filter.put("offset", pageable.getOffset());
        filter.put("size", pageable.getPageSize());

        List<ReportVO> list = reportMapper.getReportList(filter);
        int total = reportMapper.getReportTotalCount(filter);

        return new PageImpl<>(list, pageable, total);
    }

    public Map<String, Object> getReportStats() {
        return reportMapper.getReportStats();
    }
}
