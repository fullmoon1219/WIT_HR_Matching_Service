package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.JobPostVO;

@Mapper
public interface JobPostMapper {

    int registerJobPost(JobPostVO jobPostVO);
}
