package org.wit.hrmatching.controller.community;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wit.hrmatching.service.community.BoardService;
import org.wit.hrmatching.vo.community.BoardVO;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {

    private final BoardService boardService;

    @GetMapping("/{boardCode}")
    public String showBoardList(@PathVariable String boardCode) {
        return "community/board-list";
    }

    @GetMapping("/{boardCode}/write")
    public String showWritePage(@PathVariable String boardCode, Model model) {
        if ("all".equals(boardCode)) {
            boardCode = "free";
        }
        model.addAttribute("boardCode", boardCode);


        List<BoardVO> boardList = boardService.findAllBoards();
        model.addAttribute("boardList", boardList);

        return "community/board-write";
    }

    @GetMapping("/posts/view/{id}")
    public String getPostFragment(@PathVariable Long id) {

        return "community/board-view";
    }

}
