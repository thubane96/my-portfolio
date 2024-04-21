package com.icode.twitterclonebackend.services;

import com.icode.twitterclonebackend.exception.MessageNotFoundException;
import com.icode.twitterclonebackend.models.Message;
import com.icode.twitterclonebackend.models.Messages;
import com.icode.twitterclonebackend.models.User;
import com.icode.twitterclonebackend.repositories.MessageRepository;
import com.icode.twitterclonebackend.repositories.UserRepository;
import com.icode.twitterclonebackend.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class MessagingService {

    private MessageRepository messageRepo;
    private UserRepository userRepo;
    private MessageUtil messageUtil;

    @Autowired
    public MessagingService(MessageRepository messageRepo, UserRepository userRepo, MessageUtil messageUtil){
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.messageUtil = messageUtil;
    }

    public void saveMessage(String userToUsername, String messageBody, MultipartFile messageImage, String username) {

        User userSendingMessage = userRepo.findByUsername(username);
        User userReceivingMessage = userRepo.findByUsername(userToUsername);

        Messages messageSent = new Messages();
        messageSent.setOpened(false);
        messageSent.setDeleted(false);
        if (messageBody != null){
            messageSent.setMessageBody(messageBody);
        }

        if (messageImage != null){
            try {
                messageSent.setMessageImage(Base64.getEncoder().encodeToString(messageImage.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Save User From Message

        if (userSendingMessage.getMessageList().stream().anyMatch(user -> userToUsername.equals(user.getFriendMessaging()))){

            messageSent.setMessagedTo(false);
            Message messageFetched = userSendingMessage.getMessageList().stream()
                    .filter(user -> userToUsername.equals(user.getFriendMessaging()))
                    .findFirst()
                    .orElseThrow( () -> new MessageNotFoundException("Message was not found"));
            messageFetched.addMessage(messageSent);
            userSendingMessage.addMesssage(messageFetched);
            messageRepo.save(messageFetched);
        }else {
            Message message = new Message();
            messageSent.setMessagedTo(false);
            message.setFriendMessaging(userToUsername);
            userSendingMessage.addMesssage(message);
            messageRepo.save(message);

            //fetch saved object
            userSendingMessage = userRepo.findByUsername(username);
            Message messageFetched = userSendingMessage.getMessageList().stream()
                    .filter(user -> userToUsername.equals(user.getFriendMessaging()))
                    .findFirst()
                    .orElseThrow( () -> new MessageNotFoundException("Message was not found"));
            messageFetched.addMessage(messageSent);
            userSendingMessage.addMesssage(messageFetched);
            messageRepo.save(messageFetched);
        }

        //Save User To Message
        if (userReceivingMessage.getMessageList().stream().anyMatch(user -> username.equals(user.getFriendMessaging()))){
            messageSent.setMessagedTo(true);
            Message messageFetched = userReceivingMessage.getMessageList().stream()
                    .filter(user -> username.equals(user.getFriendMessaging()))
                    .findFirst()
                    .orElseThrow( () -> new MessageNotFoundException("Message was not found"));
            messageFetched.addMessage(messageSent);
            userReceivingMessage.addMesssage(messageFetched);
            messageRepo.save(messageFetched);
        }else {
            Message message = new Message();
            messageSent.setMessagedTo(true);
            message.setFriendMessaging(username);
            userReceivingMessage.addMesssage(message);
            messageRepo.save(message);

            //fetch saved object
            userReceivingMessage = userRepo.findByUsername(userToUsername);
            Message messageFetched = userReceivingMessage.getMessageList().stream()
                    .filter(user -> username.equals(user.getFriendMessaging()))
                    .findFirst()
                    .orElseThrow( () -> new MessageNotFoundException("Message was not found"));
            messageFetched.addMessage(messageSent);
            userReceivingMessage.addMesssage(messageFetched);
            messageRepo.save(messageFetched);
        }


    }

    public void messageFunction(List<Message> messagesList){
        int numOfMessagesUnopened;

        for (Message message: messagesList){
            numOfMessagesUnopened = 0;
            message.setLastMessageContent(message.getMessagesList().get(message.getMessagesList().size() - 1).getMessageBody());
            message.setTime(messageUtil.tweetTime(message.getMessagesList().get(message.getMessagesList().size() - 1)
                    .getCreatedAt().toString()));
            for (Messages messages: message.getMessagesList()){
                messages.setTime(messageUtil.tweetTime(messages.getCreatedAt().toString()));
                if (!messages.isOpened()){
                    numOfMessagesUnopened = numOfMessagesUnopened + 1;
                }
            }
            message.setNumberOfUnopenedMessages(numOfMessagesUnopened);
        }
    }

    public void openMessage(String friendMessaging, String username){

        User user = userRepo.findByUsername(username);

        if ( user.getMessageList().stream().anyMatch( friend -> friendMessaging.equals(friend.getFriendMessaging()))){

            Message messageFetched = user.getMessageList().stream()
                    .filter(friend -> friendMessaging.equals(friend.getFriendMessaging()))
                    .findFirst()
                    .orElseThrow();

            for( Messages message: messageFetched.getMessagesList()){
                message.setOpened(true);
            }

            messageRepo.save(messageFetched);
        }
    }

}
