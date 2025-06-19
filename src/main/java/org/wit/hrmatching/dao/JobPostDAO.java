package org.wit.hrmatching.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.JobPostMapper;
import org.wit.hrmatching.vo.JobPostVO;

@Repository
public class JobPostDAO {

    @Autowired
    private JobPostMapper jobPostMapper;
    public int registerJobPost (JobPostVO jobpostVO) {

        int flag = 1;
        int result = jobPostMapper.registerJobPost(jobpostVO);

        if (result == 1) {
            flag = 0;
        }
        return result;
    }

}
