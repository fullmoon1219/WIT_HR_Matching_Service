package org.wit.hrmatching.service.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.employer.TechStackMapper;
import org.wit.hrmatching.vo.job.TechStackVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechStackService {
    private final TechStackMapper techStackMapper;

    public List<TechStackVO> getAllStacks() {
        return techStackMapper.getAllTechStacks();
    }

    public List<String> getStackNamesByIds(List<Long> stackIds) {
        return techStackMapper.selectNamesByIds(stackIds);
    }
}

