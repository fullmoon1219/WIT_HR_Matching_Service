package org.wit.hrmatching.dao.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.employer.EmployerProfileMapper;

@Repository
@RequiredArgsConstructor
public class EmployerProfileDAO {

    private final EmployerProfileMapper employerProfileMapper;

}
