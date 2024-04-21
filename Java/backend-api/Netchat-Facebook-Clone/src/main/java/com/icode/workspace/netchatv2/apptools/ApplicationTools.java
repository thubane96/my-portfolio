package com.icode.workspace.netchatv2.apptools;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Configuration
public class ApplicationTools {

    public String getTimePosted(String timePosted, String timeCurrently){
        DateTimeZone timeZone = DateTimeZone.forID("America/Montreal");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yy-MM-dd HH:mm:ss").withZone(timeZone);
        DateTime dateTimeStart = formatter.parseDateTime(timePosted);
        DateTime dateTimeStop = formatter.parseDateTime(timeCurrently);
        Period period = new Period(dateTimeStart, dateTimeStop);
        PeriodFormatter periodFormatter = PeriodFormat.getDefault();
        String output = periodFormatter.print( period );
        Pattern p = Pattern.compile(",");
        String[] result = p.split(output);
        return result[0] +" ago";
    }

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
}
