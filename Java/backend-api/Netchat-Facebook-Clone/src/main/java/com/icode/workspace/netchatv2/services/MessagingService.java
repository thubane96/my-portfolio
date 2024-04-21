package com.icode.workspace.netchatv2.services;

import com.icode.workspace.netchatv2.apptools.ApplicationTools;
import com.icode.workspace.netchatv2.dto.MessageDto;
import com.icode.workspace.netchatv2.exception.MessageNotFoundException;
import com.icode.workspace.netchatv2.models.Messages;
import com.icode.workspace.netchatv2.models.Users;
import com.icode.workspace.netchatv2.repositories.MessageRepository;
import com.icode.workspace.netchatv2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessagingService {

    private MessageRepository messageRepo;
    private UserRepository userRepo;
    private UserServiceImpl userService;
    private ApplicationTools tools;

    private List<Messages> messageList = new ArrayList<Messages>();

    @Autowired
    public MessagingService(MessageRepository messageRepo,  UserServiceImpl userService, ApplicationTools tools,
                            UserRepository userRepo){
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.userService = userService;
        this.tools = tools;
    }

    public void saveMessage(MessageDto message, String username) {

        messageRepo.save(
                new Messages(
                        userService.getUsersDetails(message.getUserToId()).getUsername(),
                        username,
                        message.getBody(),
                        LocalDate.now().toString()+" "+ LocalTime.now().toString().substring(0,8),
                        "no",
                        "no"
                )
        );
    }

    public void messageTime(Messages message){
        String currentTime = LocalDate.now().toString()+" "+LocalTime.now().toString().substring(0,8);
        message.setTime(tools.getTimePosted(message.getTimeMessaged(), currentTime));
    }

    public List<Messages> getAllMessages(){
        List<Messages> messages = messageRepo.findAll();

        for (Messages message: messages){
            messageTime(message);
        }

        return messages;
    }

    public List<Messages> getMessages(String userClickedProfile, String username){

        messageList.clear();
        List<Messages> messages = messageRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));

        for (Messages message: messages){
            if (message.getUserTo().equals(userClickedProfile) && message.getUserFrom().equals(username)){
                messageTime(message);
                messageList.add(message);
            }
            else if(message.getUserTo().equals(username) && message.getUserFrom().equals(userClickedProfile)){
                messageTime(message);
                messageList.add(message);
            }
        }
        return messageList;
    }

    public Long getMessageDetails(Long messageId){
        Messages message = messageRepo.findById(messageId).orElseThrow(
                () -> new MessageNotFoundException("Message with id of "+messageId+" was not found!")
        );
        message.setOpened("yes");
        messageRepo.save(message);
        Users user = userRepo.findByUsername(message.getUserFrom());
        return user.getId();
    }

    public Integer getNumberOfMessages(String username){
        int numberOfMessages = 0;
        for (Messages message: getAllMessages()){
            if (message.getUserTo().equals(username) & message.getOpened().equals("no")){
                numberOfMessages = numberOfMessages + 1;
            }
        }
        return numberOfMessages;
    }
}
