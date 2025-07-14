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
    public String showBoardList(@PathVariable String boardCode, Model model) {
        model.addAttribute("boardCode", boardCode);
        return "community/board-list";
    }


    @GetMapping("/{boardCode}/write")
    public String showWritePage(@PathVariable String boardCode, Model model) {
        if ("all".equals(boardCode)) {
            boardCode = "free";
        }
        model.addAttribute("boardCode", boardCode);
        model.addAttribute("communityBoards", boardService.findAllBoards());
        model.addAttribute("boardList", boardService.findAllBoards());

        return "community/board-write";
    }

    @GetMapping("/posts/view/{postId}")
    public String getPostFragment(@PathVariable Long postId, Model model) {
        model.addAttribute("postId", postId);

        return "community/board-view";
    }

    @GetMapping("/{boardCode}/edit/{id}")
    public String showPostModify(@PathVariable String boardCode,@PathVariable Long id, Model model) {

        model.addAttribute("boardCode", boardCode);

        return "community/board-modify";
    }

}
