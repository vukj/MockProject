package com.fa.training.mockproject.controllers;

import com.fa.training.mockproject.entities.Employees;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fa.training.mockproject.enumeric.GenderTypes;
import com.fa.training.mockproject.enumeric.MaritalStatus;
import com.fa.training.mockproject.services.FilesUploadService;
import com.fa.training.mockproject.services.UserAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;

@Controller
@RequestMapping("/my-account")
@SessionAttributes("user")
public class MyAccountController {

    @Autowired
    private UserAccountsService userAccountsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Account Page
     *
     * @param principal
     * @param model
     * @return
     */
    @GetMapping
    public String index(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(principal.getName());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (userAccountsLoginDTO.getActiveStatus() == ActiveStatus.Inactive) {
            model.addAttribute("message", "Your account not active yet, please active your account!");
            //Logout
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
                return "/home/signin";
            }
        }
        //set last logged in
        userAccountsLoginDTO.setLastLogged(new Date());
        userAccountsService.updateLastLogged(userAccountsLoginDTO);
        model.addAttribute("user", userAccountsLoginDTO);
        for (GrantedAuthority authority : auth.getAuthorities()) {
            model.addAttribute("accountRole", authority.getAuthority());
        }
        return "/myaccount/index";
    }

    /**
     * Edit account info
     *
     * @param principal
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String editAccount(Principal principal, Model model) {
        model.addAttribute("employees", userAccountsService.findByEmail(principal.getName()).getEmployees());
        model.addAttribute("genderList", Arrays.asList(GenderTypes.values()));
        model.addAttribute("maritalList", Arrays.asList(MaritalStatus.values()));
        return "myaccount/editaccount";
    }

    /**
     * Account info updated
     *
     * @param principal
     * @param employees
     * @return
     */
    @PostMapping("updated")
    public String updatedProfile(Principal principal, @ModelAttribute("employees") Employees employees, RedirectAttributes model) {
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(principal.getName());
        userAccountsLoginDTO.getEmployees().setFullName(employees.getFullName());
        userAccountsLoginDTO.getEmployees().setAddress1(employees.getAddress1());
        userAccountsLoginDTO.getEmployees().setAddress2(employees.getAddress2());
        userAccountsLoginDTO.getEmployees().setPhone(employees.getPhone());
        userAccountsLoginDTO.getEmployees().setGenderTypes(employees.getGenderTypes());
        userAccountsLoginDTO.getEmployees().setMaritalStatus(employees.getMaritalStatus());
        userAccountsLoginDTO.getEmployees().setDateOfBirth(employees.getDateOfBirth());
        userAccountsService.saveAccount(userAccountsLoginDTO);
        model.addFlashAttribute("message", "Profile update successfully!");
        return "redirect:/my-account";
    }

    /**
     * Change avatar page
     *
     * @param principal
     * @param model
     * @return
     */
    @GetMapping("photo")
    public String updatePhoto(Principal principal, Model model) {
        model.addAttribute("user", userAccountsService.findByEmail(principal.getName()));
        return "myaccount/updatephoto";
    }

    /**
     * Save avatar
     *
     * @param multipartFile
     * @param principal
     * @return
     * @throws IOException
     */
    @PostMapping("photo-update")
    public String updatedPhoto(@RequestParam("file") MultipartFile multipartFile, Principal principal,
                               Model model) throws IOException {
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(principal.getName());
        if (userAccountsLoginDTO.getEmployees().getAvatar() != null) {
            File avatar = new File("/Users/vukj/Documents/BitBucket/mock-project/src/main/resources/static/images/" + userAccountsLoginDTO.getEmail() + "/" + userAccountsLoginDTO.getEmployees().getAvatar());
            avatar.delete();
        }
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        userAccountsLoginDTO.getEmployees().setAvatar(fileName);
        userAccountsService.updateAvatar(userAccountsLoginDTO);
        String uploadDir = "/Users/vukj/Documents/BitBucket/mock-project/src/main/resources/static/images/" + userAccountsLoginDTO.getEmail();
        try {
            FilesUploadService.saveFile(uploadDir, fileName, multipartFile);
            model.addAttribute("message", "Avatar upload successfully!");
        } catch (MaxUploadSizeExceededException e) {
            model.addAttribute("message", "Image size is too large!");
        } catch (IOException ex) {
            model.addAttribute("message", "Fail to upload the photo!");
        }
        return "redirect:" + "/my-account/photo";
    }

    /**
     * API call img avatar
     *
     * @param userAccountsLoginDTO
     * @return
     * @throws IOException
     */
    @GetMapping(value = "img", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getImg(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) throws IOException {
        File file;
        if (userAccountsLoginDTO.getEmployees().getAvatar() == null || userAccountsLoginDTO.getEmployees().getAvatar().equals("")) {
            file = new File("/Users/vukj/Documents/BitBucket/mock-project/src/main/resources/static/images/default-picture.png");
        } else {
            file = new File("/Users/vukj/Documents/BitBucket/mock-project/src/main/resources/static/images/" + userAccountsLoginDTO.getEmail() + "/" + userAccountsLoginDTO.getEmployees().getAvatar());
        }
        return Files.readAllBytes(file.toPath());
    }

    /**
     * Change password page
     *
     * @param principal
     * @param model
     * @param userAccountsLoginDTO
     * @return
     */
    @GetMapping("password")
    public String changePassword(Principal principal, Model model,
                                 @ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        model.addAttribute("email", principal.getName());
        return "myaccount/changepassword";
    }

    /**
     * Save new password
     *
     * @param userAccountsLoginDTO
     * @param modelAndView
     * @param principal
     * @return
     */
    @PostMapping("changed-password")
    public ModelAndView changedPassword(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO,
                                        ModelAndView modelAndView, Principal principal) {
        UserAccountsLoginDTO userAccounts = userAccountsService.findByEmail(userAccountsLoginDTO.getEmail());
        if (passwordEncoder.matches(userAccountsLoginDTO.getPassword(), userAccounts.getPassword())) {
            userAccounts.setPassword(passwordEncoder.encode(userAccountsLoginDTO.getNewPassword()));
            modelAndView.setViewName("myaccount/index");
            modelAndView.addObject("message", "Password change successfully!");
            userAccountsService.saveAccount(userAccounts);
        } else {
            modelAndView.addObject("email", principal.getName());
            modelAndView.setViewName("myaccount/changepassword");
            modelAndView.addObject("message", "Your old Password does not match!");
        }
        return modelAndView;
    }

    /**
     * Deactive account page
     *
     * @return
     */
    @GetMapping("deactive")
    public String deactiveAccount() {
        return "myaccount/deactiveaccount";
    }

    /**
     * Account deactivated
     *
     * @param principal
     * @param model
     * @return
     */
    @GetMapping("deactivated")
    public String deactivatedAccount(Principal principal, HttpServletRequest request, HttpServletResponse response, Model model) {
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(principal.getName());
        userAccountsService.deactiveAccount(userAccountsLoginDTO);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        model.addAttribute("message", "Your account has been deactivated!");
        return "/home/index";
    }

    @ModelAttribute
    public UserAccountsLoginDTO user(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        return userAccountsLoginDTO;
    }
}
