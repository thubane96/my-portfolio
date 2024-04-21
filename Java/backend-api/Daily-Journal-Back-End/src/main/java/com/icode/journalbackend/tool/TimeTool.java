package com.icode.journalbackend.tool;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
public class TimeTool {

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

}
