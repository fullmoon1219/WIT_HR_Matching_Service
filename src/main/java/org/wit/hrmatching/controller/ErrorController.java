package org.wit.hrmatching.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // error 페이지 처리 로직
        return "login/error";  // error.html 템플릿으로 이동
    }
}

