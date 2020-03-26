package com.isograd.exercise.y_model;

public class TimeSchedule {
    public int hour;
    public int minute;
    public TimeSchedule(int hour, int minute){

        this.hour=hour;
        this.minute=minute;
    }

    public int toTime() {
        return hour * 60 + minute;
    }

    public static TimeSchedule add59(TimeSchedule timeSchedule) {
        return TimeSchedule.fromTime(timeSchedule.toTime()+59);
    }

    public static TimeSchedule fromTime(int time) {
        return  new TimeSchedule(time/60, time%60);
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d",hour,minute);
    }

    public static TimeSchedule first() {
        return new  TimeSchedule(8,0);
    }
    public static TimeSchedule last() {
        return new TimeSchedule(17,0);
    }
}
