package org.wit.hrmatching.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/")
    public String handleError() {

        return "error/error";
    }

    @RequestMapping("/access-denied")
    public String accessDenied() {

        return "error/access-denied";
    }

    @RequestMapping("/db-access-denied")
    public String dbAccessDenied() {

        return "error/db-access-denied";
    }
}

