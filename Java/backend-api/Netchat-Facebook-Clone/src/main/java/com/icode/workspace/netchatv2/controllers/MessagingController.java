package com.icode.workspace.netchatv2.controllers;

import com.icode.workspace.netchatv2.dto.MessageDto;
import com.icode.workspace.netchatv2.services.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class MessagingController {

    static boolean shiftToMessages = false;
    private MessagingService messageService;

    @Autowired
    public MessagingController(MessagingService messageService){
        this.messageService = messageService;
    }

    @ModelAttribute("message")
    public MessageDto messageDto(){
        return new MessageDto();
    }

    @PostMapping(path = "/sendMessage")
    public String sendMessage(@ModelAttribute("message") MessageDto messageDto, Principal principal){
        messageService.saveMessage(messageDto, principal.getName());
        return "redirect:/profile/"+messageDto.getUserToId();
    }

    @GetMapping(path = "/viewMessage/{messageId}")
    public String viewMessage(@PathVariable("messageId") Long messageId, Model model){
        shiftToMessages = true;
        return "redirect:/profile/"+messageService.getMessageDetails(messageId);
    }
}
