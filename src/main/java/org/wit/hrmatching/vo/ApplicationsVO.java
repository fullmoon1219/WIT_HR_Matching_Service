package org.wit.hrmatching.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationsVO {

	private long id;
	private long userId;
	private long jobPostId;
	private long resumeId;
	private String status;		// APPLIED, ACCEPTED, REJECTED
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime appliedAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
}
