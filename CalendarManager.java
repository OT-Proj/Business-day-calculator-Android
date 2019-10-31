package com.bday;

import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;
import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;

import java.util.Calendar;

public class CalendarManager {
    // synchronizes between Gregorian calendar and  Jewish calendar

    private Calendar cal;
    private JewishCalendar jcal;
    private HebrewDateFormatter hdf;

    public CalendarManager(Calendar cal) {
        this.cal = cal;

        this.jcal = new JewishCalendar(cal);
        jcal.setInIsrael(true); // for israeli holiday scheme
        jcal.setUseModernHolidays(true); // for yamei zikaron(memorial days) and atzmaut(independence day)

        this.hdf = new HebrewDateFormatter();
    }

    public Calendar getCalender()
    {
        return cal;
    }

    public void forwardDays(int numberOfDays) {
        // moves <numberOfDays> days forward

        for(int i=0; i<numberOfDays; i++) {
            cal.add(Calendar.DATE,1); //we move to tomorrow
            jcal.forward();
        }
    }

    public String getJewishEvent()
    // returns the jewish even that occurs on the current set day
    {
        String event = this.hdf.formatYomTov(this.jcal);
        if(event.compareTo("") == 0)
            return "None";
        return event;
    }





}
