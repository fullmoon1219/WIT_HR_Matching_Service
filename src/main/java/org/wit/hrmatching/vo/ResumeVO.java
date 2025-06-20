package org.wit.hrmatching.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias(value = "resumeVO")
@Data
public class ResumeVO {

	private long id;
	private long userId;
	private String education;
	private String experience;
	private String skills;
	private String preferredLocation;
	private int salaryExpectation;
	private String createAt;
	private boolean isPublic;
	private String title;
	private String coreCompetency;
	private String desiredPosition;
	private String motivation;
	private String updatedAt;
	private String deletedAt;
}
