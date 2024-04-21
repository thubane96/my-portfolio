package com.icode.workspace.netchatv2.services;


import com.icode.workspace.netchatv2.apptools.ApplicationTools;
import com.icode.workspace.netchatv2.dto.UserSignupDto;
import com.icode.workspace.netchatv2.exception.UserNotFoundException;
import com.icode.workspace.netchatv2.models.Roles;
import com.icode.workspace.netchatv2.models.Users;
import com.icode.workspace.netchatv2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepo;
    private ApplicationTools tools;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo, ApplicationTools tools) {
        super();
        this.userRepo = userRepo;
        this.tools = tools;
    }

    @Override
    public Users save(UserSignupDto userSignupDto) {
        LocalDate dateSignedUp = LocalDate.now();
        Users user = new Users(
                userSignupDto.getFirstName(),
                userSignupDto.getLastName(),
                userSignupDto.getUsername(),
                passwordEncoder.encode(userSignupDto.getPassword()),
                dateSignedUp.toString(),
                "no",
                Arrays.asList(new Roles("ROLE_USER")));

        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = userRepo.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Roles> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public Users getUsersDetails(Long userId){
        return userRepo.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with id of "+ userId +" was not found!.")
        );
    }

    public Users getUserByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public List<Users> getAllUsers(){
        return userRepo.findAll();
    }

    public void closeAccount(Long userId){
        userRepo.deleteById(userId);
    }

    public void updateUserDetails(String firstName, String lastName, String email, String username){
        Users userToBeUpdated = userRepo.findByUsername(username);

        if (userToBeUpdated.getUsername().equals(username)){
            userToBeUpdated.setFirstName(firstName);
            userToBeUpdated.setLastName(lastName);
            userToBeUpdated.setUsername(email);
            userRepo.save(userToBeUpdated);
        }
    }

    public void updateProfilePicture(String username, MultipartFile img){
        Users userToBeUpdated = userRepo.findByUsername(username);

        try{
            userToBeUpdated.setProfilePic(Base64.getEncoder().encodeToString(img.getBytes()));
            userRepo.save(userToBeUpdated);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void updatePassword(String username, String oldPassword, String newPassword01, String newPassword02){
        Users userToBeUpdated = userRepo.findByUsername(username);

        if (passwordEncoder.matches(oldPassword, userToBeUpdated.getPassword()) && newPassword01.equals(newPassword02)){
            userToBeUpdated.setPassword(passwordEncoder.encode(newPassword01));
            userRepo.save(userToBeUpdated);
        }
    }

    public List<Users> getSearch(String keywords){
        List<Users> foundUsers = new ArrayList<>();
        foundUsers.clear();
        String fullNames = null;
        for (Users user: getAllUsers()){
            if (user.getUsername().toLowerCase().equals(keywords.toLowerCase())){
                foundUsers.add(user);
            }
            if (user.getFirstName().toLowerCase().equals(keywords.toLowerCase())){
                foundUsers.add(user);
            }
            if (user.getLastName().toLowerCase().equals(keywords.toLowerCase())){
                foundUsers.add(user);
            }
            fullNames = user.getFirstName()+" "+user.getLastName();
            if (fullNames.toLowerCase().equals(keywords.toLowerCase())){
                foundUsers.add(user);
            }
        }
        return foundUsers;
    }

}
