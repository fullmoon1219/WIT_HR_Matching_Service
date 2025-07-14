package org.wit.hrmatching.vo.resume;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PublicResumeVO {

	private long resumeId;
	private String education;
	private String phoneNumber;
	private String skills;
	private String preferredLocation;
	private boolean isPublic;
	private String title;
	private String name;
	private List<String> skillNames; // 기술 이름 목록 추가 (프론트에서 사용)
	private String applicantName;

	private long id;
	private long userId;
	private String email;
	@NotBlank(message = "경력은 필수입니다.")
	private String experience;
	@Min(value = 1, message = "희망 연봉은 1 이상이어야 합니다.")
	private int salaryExpectation;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createAt;
	@NotBlank(message = "핵심 역량은 필수입니다.")
	private String coreCompetency;
	@NotBlank(message = "희망 직무를 선택하셔야 합니다.")
	private String desiredPosition;
	@NotBlank(message = "지원 동기는 필수입니다.")
	private String motivation;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime deletedAt;
	private boolean isCompleted;
}
