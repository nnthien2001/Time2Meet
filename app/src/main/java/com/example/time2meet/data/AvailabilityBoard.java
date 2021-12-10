package com.example.time2meet.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AvailabilityBoard {
    private Date startDate;
    private Date endDate;
    private Map<Integer, Integer> availability;

    public Map<Integer, Integer> getAvailability() {
        return availability;
    }

    public void setAvailability(Map<Integer, Integer> availability) {
        this.availability = availability;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public AvailabilityBoard(Meeting meeting){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        availability = new HashMap<>();
        try {
            this.startDate = format.parse(meeting.getStartDate());
            this.endDate = format.parse(meeting.getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer interval = Math.toIntExact((endDate.getTime() - startDate.getTime()) / 3600000);
        for (int i = 0; i<interval;++i){
            availability.put(i, 0);
        }

        for (Map.Entry<Integer, ArrayList<Integer>> entry : meeting.getAttendees().entrySet()){
            Integer key = entry.getKey();
            ArrayList<Integer> available = entry.getValue();
            for (int i = 0; i < available.size(); ++i){
                availability.replace(available.get(i), availability.get(available.get(i)) + 1);
            }
        }
    }
}
