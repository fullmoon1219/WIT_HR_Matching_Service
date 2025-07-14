package org.wit.hrmatching.vo.support;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class InquiryReasonVO {

    private Long id;                  // 사유 ID
    private String name;             // 사유 이름 (예: 로그인 문제)
    private String description;      // 설명 (선택)
    private Integer sortOrder;       // 정렬 순서
    private LocalDateTime createdAt; // 생성일
}
