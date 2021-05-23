package com.fa.training.mockproject.controllers;

import com.fa.training.mockproject.data.AccountData;
import com.fa.training.mockproject.data.CompetencyRankingPattenData;
import com.fa.training.mockproject.data.MasterData;
import com.fa.training.mockproject.entities.ConfirmationToken;
import com.fa.training.mockproject.entities.EmailTemplate;
import com.fa.training.mockproject.entities.Employees;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import com.fa.training.mockproject.enumeric.GenderTypes;
import com.fa.training.mockproject.enumeric.MaritalStatus;
import com.fa.training.mockproject.services.ConfirmationTokenService;
import com.fa.training.mockproject.services.UserAccountsService;
import com.fa.training.mockproject.services.impl.EmailSenderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Controller
@RequestMapping
@SessionAttributes("user")
public class HomeController {

    @Autowired
    private UserAccountsService userAccountsService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private EmailSenderServiceImpl emailSenderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountData accountData;
    @Autowired
    private MasterData masterData;
    @Autowired
    private CompetencyRankingPattenData competencyRankingPattenData;

    /**
     * Index Page
     */

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("home/index");
    }

    /**
     * Sign in Page
     *
     * @param userAccountsLoginDTO
     * @return
     */
    @GetMapping("signin")
    public ModelAndView signIn(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        return new ModelAndView("home/signin", "user", userAccountsLoginDTO);
    }

    /**
     * Sign Up Page
     *
     * @param model
     * @return
     */
    @GetMapping("signup")
    public String signUp(Model model) {
        model.addAttribute("genderList", Arrays.asList(GenderTypes.values()));
        model.addAttribute("maritalList", Arrays.asList(MaritalStatus.values()));
        return "home/signup";
    }

    /**
     * Set password new Account and forgot password (delete token after done)
     *
     * @param modelAndView
     * @param user
     * @return
     */
    @PostMapping("/signin")
    public ModelAndView setPassword(ModelAndView modelAndView, @ModelAttribute("user") UserAccountsLoginDTO user) {
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(user.getEmail());
        userAccountsLoginDTO.setPassword(passwordEncoder.encode(user.getNewPassword()));
        userAccountsService.saveAccount(userAccountsLoginDTO);
        ConfirmationToken token = confirmationTokenService.findByEmail(userAccountsLoginDTO.getEmail());
        confirmationTokenService.delete(token);
        modelAndView.addObject("message", "Your account has been activated, Please login!");
        modelAndView.setViewName("home/signin");
        return modelAndView;
    }

    /**
     * Forgot password page
     *
     * @return
     */
    @GetMapping("forgot-password")
    public String forgotPassword() {
        return "home/forgotpassword";
    }

    /**
     * Recovery password by send email
     *
     * @param userAccountsLoginDTO
     * @param modelAndView
     * @return
     * @throws MessagingException
     */
    @PostMapping("recovery-password")
    public ModelAndView recoveryPassword(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO,
                                         ModelAndView modelAndView) throws MessagingException {
        if (userAccountsService.exitsByEmail(userAccountsLoginDTO.getEmail())) {
            ConfirmationToken confirmationToken = new ConfirmationToken(userAccountsLoginDTO.getEmail());
            confirmationTokenService.save(confirmationToken);
            EmailTemplate emailTemplate = new EmailTemplate();
//            Create email send to user
            String link = "http://localhost:8080/confirm-account?token=" + confirmationToken.getConfirmationToken();
            String content = "Dear " + userAccountsLoginDTO.getEmail() + ",<br>"
                    + "Please click the link below to recovery your password:<br>"
                    + "<h3><a href=" + link + ">Recovery Password</a></h3>"
                    + "Thank you!<br>";
            emailTemplate.setFromAddress("fpt.academy@gmail.com");
            emailTemplate.setToAddress(userAccountsLoginDTO.getEmail());
            emailTemplate.setSubject("Password recovery!");
            emailTemplate.setContent(content);
            emailSenderService.sendEmail(emailTemplate);
            modelAndView.addObject("message", "Please check mailbox to recovery password!");
            modelAndView.setViewName("home/recoverpassword");
            return modelAndView;
        } else {
            modelAndView.addObject("message", "Email does not exits!");
            modelAndView.setViewName("home/forgotpassword");
            return modelAndView;
        }
    }

    /**
     * Complete sign up and send email to verified email
     *
     * @param userAccountsLoginDTO
     * @param modelAndView
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @PostMapping("complete-signup")
    public ModelAndView completeSignUp(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO,
                                       ModelAndView modelAndView) throws MessagingException {
        if (userAccountsService.exitsByEmail(userAccountsLoginDTO.getEmail())) {
            modelAndView.addObject("genderList", Arrays.asList(GenderTypes.values()));
            modelAndView.addObject("maritalList", Arrays.asList(MaritalStatus.values()));
            modelAndView.addObject("message", "This email already exists!");
            modelAndView.setViewName("home/signup");
        } else {
            Employees employees = new Employees();
            employees.setAccount("sys_" + userAccountsLoginDTO.getEmail().split("@")[0]);
            userAccountsLoginDTO.setEmployees(employees);
            userAccountsService.saveAccount(userAccountsLoginDTO);
            ConfirmationToken confirmationToken = new ConfirmationToken(userAccountsLoginDTO.getEmail());
            confirmationTokenService.save(confirmationToken);
            EmailTemplate emailTemplate = new EmailTemplate();
//            Create email to verified account
            String link = "http://localhost:8080/confirm-account?token=" + confirmationToken.getConfirmationToken();
            String content = "Dear " + userAccountsLoginDTO.getEmployees().getFullName() + ",<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=" + link + ">VERIFY</a></h3>"
                    + "Thank you,<br>";
            emailTemplate.setFromAddress("fpt.academy@gmail.com");
            emailTemplate.setToAddress(userAccountsLoginDTO.getEmail());
            emailTemplate.setSubject("Verify your registration");
            emailTemplate.setContent(content);
            emailSenderService.sendEmail(emailTemplate);
            modelAndView.setViewName("home/completesignup");
        }
        return modelAndView;
    }

    /**
     * Sign out
     *
     * @param request
     * @param response
     * @param modelAndView
     * @return
     */
    @GetMapping("signout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        modelAndView.addObject("message", "Logout Successfully!");
        modelAndView.setViewName("/home/index");
        return modelAndView;
    }

    /**
     * set new password page after verified email
     *
     * @param modelAndView
     * @param confirmationToken
     * @return
     */
    @GetMapping("/confirm-account")
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) throws MessagingException {
        if (confirmationTokenService.existsByConfirmationToken(confirmationToken)) {
            ConfirmationToken token = confirmationTokenService.findByConfirmationToken(confirmationToken);
            UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(token.getEmail());
            Date date = token.getCreatedDate();
            Calendar tokenCalendar = Calendar.getInstance();
            tokenCalendar.setTime(date);
            tokenCalendar.add(Calendar.DATE, 1);
            Date currentDate = new Date();
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentDate);
            if (tokenCalendar.before(currentCalendar)) {
                confirmationTokenService.delete(token);
                ConfirmationToken newToken = new ConfirmationToken(userAccountsLoginDTO.getEmail());
                confirmationTokenService.save(newToken);
                EmailTemplate emailTemplate = new EmailTemplate();
//            Create email to verified account
                String link = "http://localhost:8080/confirm-account?token=" + newToken.getConfirmationToken();
                String content = "Dear " + userAccountsLoginDTO.getEmployees().getFullName() + ",<br>"
                        + "Please click the link below to verify your registration:<br>"
                        + "<h3><a href=" + link + ">VERIFY</a></h3>"
                        + "Thank you,<br>";
                emailTemplate.setFromAddress("fpt.academy@gmail.com");
                emailTemplate.setToAddress(userAccountsLoginDTO.getEmail());
                emailTemplate.setSubject("Verify your registration");
                emailTemplate.setContent(content);
                emailSenderService.sendEmail(emailTemplate);
                modelAndView.addObject("message", "The link is expired, Please check your email to get new Confirmation Link!");
                modelAndView.setViewName("/home/index");
            } else {
                modelAndView.addObject("user", userAccountsLoginDTO);
                if (userAccountsLoginDTO.getEmployees().getAccount().startsWith("sys_")) {
                    modelAndView.setViewName("/home/setpassword");
                } else {
                    userAccountsService.saveAccount(userAccountsLoginDTO);
                    confirmationTokenService.delete(token);
                    modelAndView.addObject("message", "Your account has been activated, Please login!");
                    modelAndView.setViewName("/home/index");
                }
            }
        } else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("/home/index");
        }

        return modelAndView;
    }

    /**
     * 403 Page
     *
     * @param model
     * @param principal
     * @return
     */
    @GetMapping(value = "403")
    public String accessDenied(Model model, Principal principal) {
        if (principal != null) {
            UserAccountsLoginDTO loginedUser = userAccountsService.findByEmail(principal.getName());
            model.addAttribute("user", loginedUser);
            String message = "Hi " + loginedUser.getEmployees().getFullName() //
                    + "<br> You do not have permission to access this page!";
        }
        return "errorpage/403";
    }

    /**
     * Page error Security Role check
     *
     * @param request
     * @param model
     * @return
     */
    @GetMapping("login-error")
    public String loginError(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
                if (ex.getMessage().equals("UserDetailsService returned null, which is an interface contract violation")) {
                    errorMessage = "Email Address not found!";
                }
                if (ex.getMessage().equals("Bad credentials")) {
                    errorMessage = "Password not match!";
                }
            }
        }
        model.addAttribute("message", errorMessage);
        return "home/signin";
    }

    /**
     * Master data
     *
     * @return
     */
    @GetMapping("data")
    public String createData() {
        try {
            accountData.insertAccountData();
            masterData.insertMasterData();
            competencyRankingPattenData.insertDataCompetencyRankingPatten();
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getClass());
        }
        return "home/index";
    }

    @ModelAttribute
    public UserAccountsLoginDTO user(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        return userAccountsLoginDTO;
    }
}
