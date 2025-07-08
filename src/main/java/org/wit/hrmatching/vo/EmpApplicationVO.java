package org.wit.hrmatching.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmpApplicationVO {

	private long id;
	private long userId;
	private long jobPostId;
	private long applicationId;
	private long resumeId;
	private String status;		// APPLIED, ACCEPTED, REJECTED
	private String title;
	private String applicantName;
	private String email;
	private String name;
	private String phoneNumber;
	private String gender;
	private long age;
	private String address;
	private String portfolio_url;
	private String self_intro;


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime viewedAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime appliedAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime deletedAt;

	private String education;
	private String experience;
	private String skills;
	private String preferredLocation;
	private int salaryExpectation;
	private LocalDateTime createAt;
	private boolean isPublic;
	private String coreCompetency;
	private String desiredPosition;
	private String motivation;

}
