package org.wit.hrmatching.vo.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoginHistoryVO {
    private Long id;
    private Long userId;
    private LocalDateTime loginAt;
    private String ipAddress;
    private String userAgent;
}
