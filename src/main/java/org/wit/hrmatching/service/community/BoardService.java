package org.wit.hrmatching.service.community;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.mapper.community.BoardMapper;
import org.wit.hrmatching.vo.community.BoardVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    public List<BoardVO> findAllBoards() {
        return boardMapper.findAll();
    }
}