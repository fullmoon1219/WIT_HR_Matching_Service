package org.wit.hrmatching.controller.community;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/community")
public class CommunityController {

    @GetMapping("/{boardCode}")
    public String showBoardList(@PathVariable String boardCode) {
        return "community/board-list";
    }
}
