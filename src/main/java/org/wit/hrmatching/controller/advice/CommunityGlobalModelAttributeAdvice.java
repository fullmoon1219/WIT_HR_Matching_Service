package org.wit.hrmatching.controller.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.wit.hrmatching.controller.community.CommunityController;
import org.wit.hrmatching.service.community.BoardService;
import org.wit.hrmatching.vo.community.BoardVO;

import java.util.List;

@ControllerAdvice(assignableTypes = {CommunityController.class})
@RequiredArgsConstructor
public class CommunityGlobalModelAttributeAdvice {

    private final BoardService boardService;

    @ModelAttribute("communityBoards")
    public List<BoardVO> injectCommunityBoards() {
        return boardService.findAllBoards();
    }
}
