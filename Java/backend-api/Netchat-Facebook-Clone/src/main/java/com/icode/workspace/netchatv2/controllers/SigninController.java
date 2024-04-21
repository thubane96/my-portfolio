package com.icode.workspace.netchatv2.controllers;

import com.icode.workspace.netchatv2.dto.UserSignupDto;
import com.icode.workspace.netchatv2.models.Users;
import com.icode.workspace.netchatv2.repositories.UserRepository;
import com.icode.workspace.netchatv2.services.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/signup")
public class SigninController {

    private UserServiceImpl userService;
    private UserRepository userRepo;

    public SigninController(UserServiceImpl userService, UserRepository userRepo) {
        super();
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @ModelAttribute("user")
    public UserSignupDto userSignupDto() {
        return new UserSignupDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "signup";
    }

    @PostMapping
    public String signupUserAccount(@ModelAttribute("user") UserSignupDto signupDto) {
        String status = "errorMail";
        String regex = "^(?=.*[0-9])" +
                "(?=.*[a-z])(?=.*[A-Z])" +
                "(?=.*[@#$%^&+=])"+
                "(?=\\S+$).{8,20}$";

        Pattern p = Pattern.compile(regex);

        Users foundUserName = userRepo.findByUsername(signupDto.getUsername());
        if(foundUserName == null){
            Matcher m = p.matcher(signupDto.getPassword());
            if (m.matches()){
                userService.save(signupDto);
                status = "success";
                return "redirect:/signin?"+status;
            }else if(!m.matches()){
                status = "errorPassword";
            }
        }
        return "redirect:/signup?" + status;
    }
}
