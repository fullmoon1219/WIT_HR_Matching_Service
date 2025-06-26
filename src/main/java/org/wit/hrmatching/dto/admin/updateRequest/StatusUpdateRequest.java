package org.wit.hrmatching.dto.admin.updateRequest;

import lombok.Data;

import java.util.List;

@Data
public class StatusUpdateRequest {
    private List<Long> userIds;
    private String status;
}
