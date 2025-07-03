package org.wit.hrmatching.service.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.employer.EmpApplicationDAO;

@Service
@RequiredArgsConstructor
public class EmpApplicationService {

    private EmpApplicationDAO empApplicationDAO;
}
