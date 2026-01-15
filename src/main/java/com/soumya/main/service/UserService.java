package com.soumya.main.service;

import com.soumya.main.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    User saveUserDetails(User user, MultipartFile multipartFile) throws IOException;
    Optional<User> findUserByEmail(String email);
    User setRestTokenForForgottenPassword(String email,String resetToken);
    User resetPasswordThroughToken(String token,String password);
}
