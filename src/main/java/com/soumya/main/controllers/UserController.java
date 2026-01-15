package com.soumya.main.controllers;

import com.soumya.main.config.CustomUserDetails;
import com.soumya.main.entity.User;
import com.soumya.main.repository.UserRepository;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/profile")
    public String openUserProfilePage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Model model){
        if (customUserDetails!=null){
            Long loggedInUserId=customUserDetails.getId();
            Optional<User> user =userRepository.findById(loggedInUserId);

            user.ifPresent(loggedInClientInformation -> model.addAttribute("user", loggedInClientInformation));
            model.addAttribute("errorMsg","No User Details Found");
        }else {
            model.addAttribute("errorMsg","No User Details Found");
        }
        return "profile-page";
    }
}
