package org.wit.hrmatching.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Alias("jobpostVO")
public class JobPostVO {

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String requiredSkills;
    private Integer salary;
    private String location;
    private LocalDate deadline;
    private LocalDateTime createAt;
    private Long viewCount;
    private Long bookmarkCount;
    private EmploymentType employmentType;
    private String tags;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public enum EmploymentType {
        FULLTIME,
        PARTTIME,
        INTERN,
        FREELANCE


    }
}
