package org.wit.hrmatching.vo.support;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportVO {
    private Long id;
    private Long reporterUserId;
    private Long reportedUserId;
    private String reportType;     // USER, JOB_POST, COMMUNITY_POST
    private Long targetId;
    private String reason;
    private String description;
    private LocalDateTime reportDate;
    private String status;         // PENDING, REVIEWED, DISMISSED
    private Long reviewedByAdminId;
    private LocalDateTime reviewedAt;

    // 추가: 사용자 이름/이메일용 필드 (JOIN 용도)
    private String reporterName;
    private String reporterEmail;
}
