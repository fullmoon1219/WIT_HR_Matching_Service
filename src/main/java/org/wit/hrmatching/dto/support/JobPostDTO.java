package org.wit.hrmatching.dto.support;

import lombok.Data;

@Data
public class JobPostDTO {
    private Long id;
    private Long userId;
    private String companyName;
    private String title;
    private String description;
    private String employmentType;
    private String location;
    private String salary;
    private String deadline;
    private String jobCategory;
    private boolean deleted;
    private String createdAt;
}