package com.icode.workspace.netchatv2.services;

import com.icode.workspace.netchatv2.dto.UserSignupDto;
import com.icode.workspace.netchatv2.models.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Users save(UserSignupDto userSignupDto);
}
