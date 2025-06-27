package org.wit.hrmatching.dao.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.employer.EmployerProfileMapper;
import org.wit.hrmatching.vo.JobPostVO;

@Repository
@RequiredArgsConstructor
public class ProfileDAO {

    private final EmployerProfileMapper employerProfileMapper;



}
