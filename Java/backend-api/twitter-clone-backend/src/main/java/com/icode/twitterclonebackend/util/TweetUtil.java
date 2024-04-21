package com.icode.twitterclonebackend.util;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Component
public class TweetUtil {

    // compress the image bytes before storing it in the database
    public byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }
    // uncompress the image bytes before returning it to the angular application
    public byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }

    public String tweetTime(String createdAt){

        String period = "";

        String time = createdAt.substring(11,16);
        String date = createdAt.substring(0,10);

        if (Integer.parseInt(time.substring(0,2)) >= 12 && Integer.parseInt(time.substring(0,2)) <= 23){
            time = time +" pm";
        }else{
            time = time +" am";
        }

        switch (Integer.parseInt(date.substring(5,7))){
            case 1:
                period = "Jan";
                break;
            case 2:
                period = "Feb";
                break;
            case 3:
                period = "Mar";
                break;
            case 4:
                period = "Apr";
                break;
            case 5:
                period = "May";
                break;
            case 6:
                period = "Jun";
                break;
            case 7:
                period = "Jul";
                break;
            case 8:
                period = "Aug";
                break;
            case 9:
                period = "Sep";
                break;
            case 10:
                period = "Oct";
                break;
            case 11:
                period = "Nov";
                break;
            case 12:
                period = "Dec";
                break;
            default:
                System.out.println("went out of bound");
                break;
        }

        return  time+ " "+ period +" "+date.substring(8,10)+", "+ date.substring(0,4);
    }
}
