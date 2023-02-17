package com.socialstock.controllers;

import com.socialstock.clients.Authorization;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("auth/v1")
public class AuthController {
    @GetMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password) throws SQLException, ClassNotFoundException {
        Authorization authorization = new Authorization();
        if (authorization.login(email, password)) {
            return "Auth successful";
        }
        return "Auth unsuccessful";
    }
    @GetMapping("/register")
    public String register(@RequestParam("email") String email,
                        @RequestParam("password") String password) throws SQLException, ClassNotFoundException {
        Authorization authorization = new Authorization();
        if (authorization.register(email, password)) {
            return "Reg successful";
        }
        return "Reg unsuccessful";
    }
}
