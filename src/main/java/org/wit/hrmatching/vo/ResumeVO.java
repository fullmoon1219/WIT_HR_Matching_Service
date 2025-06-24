package org.wit.hrmatching.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResumeVO {

	private long id;
	private long userId;
	private String email;
	@NotBlank(message = "학력은 필수입니다.")
	private String education;
	@NotBlank(message = "경력은 필수입니다.")
	private String experience;
	@NotBlank(message = "기술 스택을 선택하셔야 합니다.")
	private String skills;
	@NotBlank(message = "희망 지역을 선택하셔야 합니다.")
	private String preferredLocation;
	@Min(value = 1, message = "희망 연봉은 1 이상이어야 합니다.")
	private int salaryExpectation;
	private String createAt;
	private boolean isPublic;
	@NotBlank(message = "제목은 필수입니다.")
	private String title;
	@NotBlank(message = "핵심 역량은 필수입니다.")
	private String coreCompetency;
	@NotBlank(message = "희망 직무를 선택하셔야 합니다.")
	private String desiredPosition;
	@NotBlank(message = "지원 동기는 필수입니다.")
	private String motivation;
	private String updatedAt;
	private String deletedAt;
	private boolean isCompleted;


	public boolean getIsCompleted() {
		return isCompleted;
	}

	public boolean getIsPublic() {
		return isPublic;
	}
}
