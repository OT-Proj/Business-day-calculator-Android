package com.bday;

import java.util.Date;

public class InputFieldValues {

    private Date startDate;
    private int howManyDays;
    private boolean fromSameDay;
    private boolean fridayIsBusinessDay;
    private boolean holHaMoedIsBusinessDay;
    private Date endDate;

    public InputFieldValues(Date startDate, int howManyDays, boolean fromSameDay, boolean fridayIsBusinessDay, boolean holHaMoedIsBusinessDay){
        this.startDate = startDate;
        this.howManyDays = howManyDays;
        this.fromSameDay = fromSameDay;
        this.fridayIsBusinessDay = fridayIsBusinessDay;
        this.holHaMoedIsBusinessDay = holHaMoedIsBusinessDay;
        this.endDate = null;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getHowManyDays() {
        return howManyDays;
    }

    public void setHowManyDays(int howManyDays) {
        this.howManyDays = howManyDays;
    }

    public boolean isFromSameDay() {
        return fromSameDay;
    }

    public void setFromSameDay(boolean fromSameDay) {
        this.fromSameDay = fromSameDay;
    }

    public boolean isFridayIsBusinessDay() {
        return fridayIsBusinessDay;
    }

    public void setFridayIsBusinessDay(boolean fridayIsBusinessDay) {
        this.fridayIsBusinessDay = fridayIsBusinessDay;
    }

    public boolean isHolHaMoedIsBusinessDay() {
        return holHaMoedIsBusinessDay;
    }

    public void setHolHaMoedIsBusinessDay(boolean holHaMoedIsBusinessDay) {
        this.holHaMoedIsBusinessDay = holHaMoedIsBusinessDay;
    }
}
