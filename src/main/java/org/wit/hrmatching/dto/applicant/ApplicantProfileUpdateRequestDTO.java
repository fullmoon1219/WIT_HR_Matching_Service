package org.wit.hrmatching.dto.applicant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicantProfileUpdateRequestDTO {

	private String name;
	private Integer age;
	private String address;
	private String phoneNumber;
	private String gender;
	private String portfolioUrl;
	private String selfIntro;
	private String jobType;
	private Integer experienceYears;

	private String password;
	private long userId;
}
