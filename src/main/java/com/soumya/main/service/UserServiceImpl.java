package com.soumya.main.service;

import com.soumya.main.cloudinary.CloudinaryConfig;
import com.soumya.main.entity.User;
import com.soumya.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public CloudinaryConfig cloudinaryConfig;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public User saveUserDetails(User user, MultipartFile multipartFile) throws IOException {
        String DEFAULT_PROFILE_PIC="https://res.cloudinary.com/dlcckvlfx/image/upload/v1768246559/defaultProfile_dmidkk.jpg";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setIsEnabled(true);
        if (multipartFile.isEmpty()){
            user.setImage(DEFAULT_PROFILE_PIC);
        }else {
            String uploadedImage=cloudinaryConfig.uploadImage(multipartFile);
            user.setImage(uploadedImage);
        }
        return userRepository.save(user);
    }
    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User setRestTokenForForgottenPassword(String email, String resetToken) {
        Optional<User> optionalUser=userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            User user=optionalUser.get();
            user.setResetToken(resetToken);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User resetPasswordThroughToken(String token, String password) {
        Optional<User> optionalUser=userRepository.findByResetToken(token);
        if (optionalUser.isPresent()){
            User user=optionalUser.get();
            user.setPassword(passwordEncoder.encode(password));
            user.setResetToken(null);
            return userRepository.save(user);
        }
        return null;
    }
}
