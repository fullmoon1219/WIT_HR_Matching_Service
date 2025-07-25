package org.wit.hrmatching.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantProfilesVO {

    private Long userId;           // user_id (foreign key, applicant user)
    private Integer age;           // age
    private String address;        // address
    private String phoneNumber;    // phone_number
    private String gender;         // gender (enum: 'MALE', 'FEMALE', 'OTHER')
    private String portfolioUrl;   // portfolio_url
    private String selfIntro;      // self_intro
    private String jobType;        // job_type (enum: 'FULLTIME', 'PARTTIME', 'INTERNSHIP')
    private Integer experienceYears; // experience_years
    private Long primaryResumeId;  // primary_resume_id (foreign key to resume table)
}
