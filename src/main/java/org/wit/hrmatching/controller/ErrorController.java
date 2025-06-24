package org.wit.hrmatching.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/home")
    public String handleError(HttpServletRequest request) {
        int status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status == HttpStatus.NOT_FOUND.value()) {
            return "error/not-found";
        }
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

