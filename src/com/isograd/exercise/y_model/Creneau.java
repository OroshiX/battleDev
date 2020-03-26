package com.isograd.exercise.y_model;

import java.util.List;

public class Creneau implements Comparable<Creneau> {
    public int day;
    public TimeSchedule timeScheduleStart;
    public TimeSchedule timeScheduleEnd;
    public Creneau(int day, TimeSchedule timeScheduleStart, TimeSchedule timeScheduleEnd) {
        this.day = day;
        this.timeScheduleStart = timeScheduleStart;
        this.timeScheduleEnd = timeScheduleEnd;
    }

    public static Creneau createFromStart(int day, int h, int m){
        return new Creneau(day, new TimeSchedule(h,m), TimeSchedule.add59(new TimeSchedule(h,m)));
    }

    public static Creneau createFromStart(int day, int time) {
        TimeSchedule time1 = TimeSchedule.fromTime(time);
        return Creneau.createFromStart(day, time1.hour, time1.minute);
    }

    public boolean isIncompatible(TimeSchedule timeSchedule) {
        int t = timeSchedule.toTime();
        return t >= timeScheduleStart.toTime() && t <= timeScheduleEnd.toTime();
    }

    @Override
    public String toString() {
        return day+" "+timeScheduleStart+"-"+timeScheduleEnd;
    }

    public boolean isIncompatible(List<Creneau> dayList) {
        return false;
    }

    @Override
    public int compareTo(Creneau o) {
        if (o.day > day) return -1;
        if(o.day < day) return 1;
        return timeScheduleStart.toTime() - o.timeScheduleStart.toTime();
    }

    public boolean isIncompatible(int day, int startTime) {
        if(day != this.day) return false;
        int end = startTime+59;
        if(end < timeScheduleStart.toTime()) return false;
        if(startTime > timeScheduleEnd.toTime()) return false;

        return true;
    }

    public boolean isIncompatible(Creneau creneau) {
        return false;
    }
}
