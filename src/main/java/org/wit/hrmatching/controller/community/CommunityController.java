package org.wit.hrmatching.controller.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/community")
public class CommunityController {

    @GetMapping("/all")
    public String all() {
        return "community/all";
    }

    @GetMapping("/free")
    public String free() {
        return "community/free";
    }

    @GetMapping("/qna")
    public String qna() {
        return "community/qna";
    }

    @GetMapping("/study")
    public String study() {
        return "community/study";
    }

    @GetMapping("/feedback")
    public String feedback() {
        return "community/feedback";
    }

    @GetMapping("/job-share")
    public String jobShare() {
        return "community/job-share";
    }
}
