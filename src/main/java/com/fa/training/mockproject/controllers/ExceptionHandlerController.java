package com.fa.training.mockproject.controllers;

import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.exceptions.TemplateProcessingException;

import javax.servlet.http.HttpSession;

@ControllerAdvice
@SessionAttributes("user")
public class ExceptionHandlerController {

    @ExceptionHandler({NullPointerException.class, TemplateProcessingException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String serverRequestException(HttpSession httpSession, Model model) {
        UserAccountsLoginDTO userAccountsLoginDTO = (UserAccountsLoginDTO) httpSession.getAttribute("user");
        model.addAttribute("user", userAccountsLoginDTO);
        return "/errorpage/500";
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String badRequest(NoHandlerFoundException ex, HttpSession httpSession, Model model) {
        UserAccountsLoginDTO userAccountsLoginDTO = (UserAccountsLoginDTO) httpSession.getAttribute("user");
        model.addAttribute("user", userAccountsLoginDTO);
        ex.printStackTrace();
        return "/errorpage/404";
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public String handleFileSizeLimitExceeded(FileSizeLimitExceededException exc, RedirectAttributes model) {
        model.addFlashAttribute("message", "File size too large!");
        return "redirect:/my-account/photo";
    }

    @ModelAttribute
    public UserAccountsLoginDTO user(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        return userAccountsLoginDTO;
    }
}