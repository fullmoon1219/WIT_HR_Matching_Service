package org.wit.hrmatching.vo.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ApplicantProfileImageVO {

	private long id;              // 파일 고유 ID
	private long userId;          // 어떤 유저가 올렸는지
	private final String relatedType;   // 파일 종류 (여기서는 "ETC")
	private long relatedId;       // 연관된 ID (여기서는 userId)
	private String originalName;  // 원본 파일 이름
	private String storedName;    // 서버에 저장될 고유한 이름
	private long fileSize;        // 파일 크기
	private String uploadPath;    // 파일이 저장된 폴더 경로
	private LocalDate uploadedAt; // 파일 저장 시각

	public ApplicantProfileImageVO() {
		this.relatedType = "ETC";
	}
}
