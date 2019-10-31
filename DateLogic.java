package com.bday;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateLogic {
    public static Date findDay(Date startDay, int howManyDays, boolean fromSameDay, boolean fridayIsBusinessDay, boolean holHaMoedIsBusinessDay)
    {
        // input: a date and a number standing for how many business to add
        // output: a date that represents the day when the said number of business days passes.
        // E.G: findDay(new Date(12.10.2018),1,False,False,False) returns (new Date(14.10.2018) as the former date is a friday

        Calendar cal = Calendar.getInstance(); //new calendar
        cal.setTime(startDay);
        CalendarManager cm = new CalendarManager(cal);

        if(fromSameDay && isBusinessDay(cm, fridayIsBusinessDay, holHaMoedIsBusinessDay)) { // even if "begin from today" is selected, we still need to check if its really a business day - or else we might miscalculate.
            howManyDays--;
        } //if we start from today, the first business day is already behind us.

        while(howManyDays > 0)
        {
            cm.forwardDays(1);
            if(isBusinessDay(cm , fridayIsBusinessDay, holHaMoedIsBusinessDay)) { howManyDays--; } // if its a business day, one day is behind us
        }

        return cal.getTime();
    }
    public static String formatDateToDDMMYYYY(Date d){
        return (new SimpleDateFormat("dd/MM/yyyy")).format(d);
    }

    public static Date formatDDMMYYYYToDate(String s)
    {
        // s = "dd/MM/YYYY"
        // if given a String of different format, it will return the current date

        try
        {
            int day = Integer.parseInt(s.substring(0,2));
            int month = Integer.parseInt(s.substring(3,5));
            int year = Integer.parseInt(s.substring(6,10));

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = sdf.parse(s);

            return d;
        }
        catch (Exception E)
        {
            System.out.println("DateLogic: could not parse given string to DDMMYYYY");
            return new Date();
        }
    }

    public static boolean isBusinessDay(CalendarManager cm, boolean fridayIsBusinessDay, boolean holHaMoedIsBusinessDay)
    {
        if(cm.getCalender().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) { return false; } //saturday is never a workday. thank you god.
        if(cm.getCalender().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && !fridayIsBusinessDay ) { return false; }
        if(! jewishEventIsBusinessday(cm,holHaMoedIsBusinessDay)){ return false;} //check if its a holiday

        return true;
    }

    public static boolean jewishEventIsBusinessday(CalendarManager cm, boolean holHamoed)
    {

        boolean erevHag = false; // for later compatibility; for now we don't work in erev hag.

        String event = cm.getJewishEvent();
        switch (event){
            case "None": return true; //for better performance, we would like to exit the function as soon as possible
            case "Erev Rosh Hashana": return erevHag?true:false;
            case "Hashana": return false;
            case "Erev Yom Kippur": return erevHag?true:false;
            case "Yom Kippur": return false;
            case "Erev Succos": return erevHag?true:false;
            case "Succos": return false;
            case "Chol Hamoed Succos": return holHamoed?true:false;
            case "Simchas Torah": return false;
            case "Erev Pesach": return erevHag?true:false;
            case "Pesach": return false;
            case "Chol Hamoed Pesach": return holHamoed?true:false;
            case "Yom Hazikaron": return false;
            case "Yom Ha'atzmaut": return false;
            case "Erev Shavuos": return erevHag?true:false;
            case "Shavuos": return false;
        }
        return true; // this line shouldn't be reached normally. sadly, we do work every day by default!
    }

    public static String formatTitle(int businessDays){
        String result = "";
        result+= businessDays;
        result+= " ";
        result+= "ימי עסקים יחלפו ב:";
        return result;
    }

    public static String translateDayNameToHebrew(int day)
    {
        switch(day){
            case 0: return "יום ראשון";
            case 1: return "יום שני";
            case 2: return "יום שלישי";
            case 3: return "יום רביעי";
            case 4: return "יום חמישי";
            case 5: return "יום שישי";
            case 6: return "יום שבת";
        }
        return "יום";

    }
}
