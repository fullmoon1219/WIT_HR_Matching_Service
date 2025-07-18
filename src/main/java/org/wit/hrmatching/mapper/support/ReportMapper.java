package org.wit.hrmatching.mapper.support;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.support.ReportVO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    List<ReportVO> getReportList(Map<String, Object> filter);
    int getReportTotalCount(Map<String, Object> filter);

    Map<String, Object> getReportStats();

    void insertReport(ReportVO report);
}
