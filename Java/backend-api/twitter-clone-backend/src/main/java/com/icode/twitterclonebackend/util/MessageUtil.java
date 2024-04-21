package com.icode.twitterclonebackend.util;

import org.springframework.stereotype.Component;

@Component
public class MessageUtil {

    public String tweetTime(String createdAt){

        String period = "";

        String time = createdAt.substring(11,16);
        String date = createdAt.substring(0,10);

        if (Integer.parseInt(time.substring(0,2)) >= 12 && Integer.parseInt(time.substring(0,2)) <= 23){
            time = time +" pm";
        }else{
            time = time +" am";
        }

        return  time;
    }

}
