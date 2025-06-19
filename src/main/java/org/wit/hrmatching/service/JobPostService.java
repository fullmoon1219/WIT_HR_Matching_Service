package org.wit.hrmatching.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.JobPostDAO;
import org.wit.hrmatching.vo.JobPostVO;

@Service
public class JobPostService {

    @Autowired
    private JobPostDAO jobPostDAO;

    public int registerJobPost(JobPostVO jobPostVO) {
        return jobPostDAO.registerJobPost(jobPostVO);
    }
}
