package org.wit.hrmatching.controller.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/employer")
@RequiredArgsConstructor
public class EmployerControllerTest {

    @GetMapping("/1")
    public String main() {

        return "employer/";
    }
}
