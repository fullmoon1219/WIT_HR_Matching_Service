package org.wit.hrmatching.controller.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/publicResume")
public class PublicResumesController {

    @GetMapping("/search")
    public String search() {
        return "employer/publicResume/search";
    }

}
