package com.icode.workspace.netchatv2.services;

import com.icode.workspace.netchatv2.apptools.ApplicationTools;
import com.icode.workspace.netchatv2.models.Notifications;
import com.icode.workspace.netchatv2.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NotificationService {

    private NotificationRepository notificationRepo;
    private ApplicationTools applicationTools;

    private List<Notifications> notificationsList = new ArrayList<Notifications>();


    @Autowired
    public NotificationService(NotificationRepository notificationRepo, ApplicationTools applicationTools){
        this.notificationRepo = notificationRepo;
        this.applicationTools = applicationTools;
    }

    public void saveNotification(Long postId, String userTo, String userFrom, String message){
        notificationRepo.save(
                new Notifications(
                        userTo,
                        userFrom,
                        message,
                        postId,
                        LocalDate.now().toString()+" "+ LocalTime.now().toString().substring(0,8),
                        "no"
                )

        );

    }

    public List<Notifications> getNotifications(String username){
        List<Notifications> notifications = notificationRepo.findAll();
        notificationsList.clear();

        for (Notifications notification: notifications){
            if (notification.getUserTo().equals(username) && notification.getOpened().equals("no")){
                String timeNow = LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8);
                notification.setTime(applicationTools.getTimePosted(notification.getTimeAdded(), timeNow));
                notificationsList.add(notification);
            }
        }
        return notificationsList;
    }

    public void removeNotification(Long notificationId){
        notificationRepo.deleteById(notificationId);
    }


}














