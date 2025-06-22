package org.wit.hrmatching.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String userManagementPage() {
        return "admin/user-management";
    }
}
