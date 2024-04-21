package com.icode.journalbackend;

import java.time.LocalDate;
import java.time.LocalTime;

public class Demo {

    public static void main(String[] arg){
        System.out.println("Time: "+ LocalTime.now().toString().substring(0, 8));
        System.out.println("Date: "+ LocalDate.now().toString());
    }
}
