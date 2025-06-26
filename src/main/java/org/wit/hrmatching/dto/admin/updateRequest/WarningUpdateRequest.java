package org.wit.hrmatching.dto.admin.updateRequest;

import lombok.Data;

import java.util.List;

@Data
public class WarningUpdateRequest {
    private List<Long> userIds;
    private int count;
}
