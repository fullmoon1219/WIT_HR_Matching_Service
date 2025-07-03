package org.wit.hrmatching.service.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.employer.TechStackMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechStackService {
    private final TechStackMapper techStackMapper;

    public List<String> getAllStacks() {
        return techStackMapper.getAllTechStacks();
    }
}

