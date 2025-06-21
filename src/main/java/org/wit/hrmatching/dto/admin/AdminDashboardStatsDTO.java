package org.wit.hrmatching.dto.admin;

import lombok.Data;

@Data
public class AdminDashboardStatsDTO {
    private long userCount;
    private int applicantCount;
    private int companyCount;
    private long resumeCount;
    private long jobPostCount;
    private long applicationCount;
}
