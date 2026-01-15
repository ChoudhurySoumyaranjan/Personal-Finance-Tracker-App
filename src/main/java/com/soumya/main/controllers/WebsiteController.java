package com.soumya.main.controllers;

import com.soumya.main.config.CustomUserDetails;
import com.soumya.main.entity.User;
import com.soumya.main.service.UserService;
import com.soumya.main.utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@SessionAttributes({"loggedInUser"})
public class WebsiteController {

    @Autowired
    public UserService userService;

    @Autowired
    public CommonUtils commonUtils;

    @RequestMapping({"/","/index"})
    public String openIndexPage(){
        return "index-page";
    }
    @GetMapping("/signIn")
    public String openLoginPage(Model model){
        return "login-page";
    }

    @GetMapping("/register")
    public String openRegistrationPage(Model model){
        model.addAttribute("user",new User());
        return "register-page";
    }

    @PostMapping("/handleRegistrationForm")
    public String handleRegistrationForm(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            @RequestParam("img") MultipartFile multipartFile,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        if (bindingResult.hasErrors()) {
            return "register-page";
        } else {
            User savedUserDetails =userService.saveUserDetails(user,multipartFile);
            if (savedUserDetails != null) {
                redirectAttributes.addFlashAttribute("successMsg", "You are Registered Successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "Failed to Register");
            }
        }

        return "redirect:/register";
    }

    @ModelAttribute  //this method will be called when any request hit to end point of this application controller
    public void loggedInUserDetails(Authentication authentication, Model model){

        if(authentication!=null) {

            CustomUserDetails loggedInUser = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("loggedInUser",loggedInUser);
                                    //after user successfully logged in through spring security details stored in Spring Security Context
        }

    }

    @PostMapping("/logout")
    public String logoutPage(SessionStatus sessionStatus){
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @GetMapping("/resetPassword")
    public  String openRestPasswordEmailPage(){
        return "reset-page-email";
    }
    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam(value = "token",required = false)String token,
                                Model model){
        model.addAttribute("token",token);
        return "reset-password-page";
    }

    @PostMapping("/passwordResetField")
    public String handlePasswordRequestEmail(@RequestParam("email")String email,
                                             RedirectAttributes redirectAttributes,
                                             Model model,
                                             HttpServletRequest request){

        Optional<User> optionalUser=userService.findUserByEmail(email);
        if (optionalUser.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMsg","Please Enter Valid Email");
            return "redirect:/resetPassword";
        }else{
            String resetToken=CommonUtils.generateResetToken();
            User user=userService.setRestTokenForForgottenPassword(email,resetToken);
            if (user!= null) {
                String url = CommonUtils.generateUrl(request) + "/reset-password?token=" + resetToken;
                //generatedToken = http://localhost:1010/reset-password?token=generatedToken

                boolean success = commonUtils.sendResetEmail(email, url);

                if (success) {
                    redirectAttributes.addFlashAttribute("successMsg", "Reset Link sent Successfully");
                }else {
                    redirectAttributes.addFlashAttribute("errorMsg","Internal Server Error Occured During Email Sending");
                }
            }
        }
        return "redirect:/resetPassword";
    }

    @PostMapping("/resetPasswordForm")
    public String attemptResetPasswordUsingToken(@RequestParam("password")String password,
                                                 @RequestParam(value = "token",required = false)String token,
                                                 RedirectAttributes redirectAttributes){
        if (token!=null) {
            User user = userService.resetPasswordThroughToken(token, password);

            if (user!=null) {
                redirectAttributes.addFlashAttribute("successMsg", "Password Reset Successfully");
            }

        }else {
            redirectAttributes.addFlashAttribute("errorMsg","Failed to Reset Password Token Missing or invalid");
        }
        return "redirect:/reset-password";
    }

}
