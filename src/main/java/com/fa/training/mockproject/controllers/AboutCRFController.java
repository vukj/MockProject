package com.fa.training.mockproject.controllers;

import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("about-crf")
@SessionAttributes("user")
public class AboutCRFController {

    @GetMapping
    public String index() {
        return "/aboutcrf/aboutcrf";
    }

    @ModelAttribute
    public UserAccountsLoginDTO user(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        return userAccountsLoginDTO;
    }
}

