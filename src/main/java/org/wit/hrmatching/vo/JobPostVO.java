package org.wit.hrmatching.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class JobPostVO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String requiredSkills;
    private Integer salary;
    private String location;
    private LocalDate deadline;
    private String employmentType;
    private String tags;
    private String jobCategory;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Long viewCount;
    private Long bookmarkCount;

    private String companyName;
}
