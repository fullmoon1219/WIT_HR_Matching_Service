package org.wit.hrmatching.vo.admin;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminMemoVO {
    private Long id;
    private Long userId;          // 작성한 관리자 ID
    private String content;
    private LocalDateTime createdAt;
    private String adminName;
    private String adminEmail;
}
