package org.wit.hrmatching.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Alias(value = "jobpostVO")
@Data
public class JobPostVO {

    public enum EmploymentType {
        FULLTIME, PARTTIME, INTERN, FREELANCE
    }

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String requiredSkills;
    private Integer salary;
    private String location;
    private LocalDate deadline;

    private EmploymentType employmentType;
//    private String employmentType;

    private String jobCategory;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Long viewCount;
    private Long bookmarkCount;

    private String companyName;

    private boolean isCompleted;
}
