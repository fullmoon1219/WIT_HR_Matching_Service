package org.wit.hrmatching.vo.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Alias(value = "jobpostVO")
@Data
public class JobPostVO {

    public enum EmploymentType {
        FULLTIME, PARTTIME, INTERN, FREELANCE
    }
    public enum  ExperienceType{
        NEWCOMER, EXPERIENCED, ANY
    }

    private Long id;
    private Long userId;
    private Long applicantCount;

    private String title;
    private String description;
    private String requiredSkills;
    private String salary;
    private String ceoName;
    private String location;
    private String workplaceAddress;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    private EmploymentType employmentType;

    private String jobCategory;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;
    private Long viewCount;
    private Long bookmarkCount;

    private String companyName;

    private boolean isCompleted;

    private long totalCount;
    private long activeCount;
    private long expiredCount;

    private String techStacks; // 콤마로 구분된 기술 스택들 저장
    private String applicantCntByJobpost; // 공고당 지원자 수

    private ExperienceType experienceType;
    private String experienceYears;
}
