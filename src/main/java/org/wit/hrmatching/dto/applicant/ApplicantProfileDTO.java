package org.wit.hrmatching.dto.applicant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApplicantProfileDTO {

	private String email;
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime createAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastLogin;
	private String profileImage;
	private boolean emailVerified;
	private String loginType;

	private int age;
	private String gender;
	private String address;
	private String phoneNumber;
	private String portfolioUrl;
	private String selfIntro;
	private String jobType;
	private int experienceYears;

	private long primaryResumeId;
	private String resumeTitle;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime resumeUpdatedAt;
}
