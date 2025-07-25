package org.wit.hrmatching.vo.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RecruitListVO {

	private long jobPostId;
	private String jobPostTitle;
	private String companyName;
	private String location;
	private String experienceType;		// NEWCOMER, EXPERIENCED, ANY
	private String salary;
	private String employmentType;		// FULLTIME, PARTTIME, INTERN, FREELANCE
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate deadline;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime createAt;
}
