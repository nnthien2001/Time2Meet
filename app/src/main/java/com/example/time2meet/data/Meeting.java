package com.example.time2meet.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Meeting {
    private Integer meetingID;
    private String meetingName;
    private Integer hostID;
    private String location;
    private String description;
    private String startDate;
    private String endDate;
    private String date;
    private Map<Integer, ArrayList<Integer>> attendees;
    private Boolean isPrivate;
    private Integer version = 1;

    public Integer getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(Integer meetingID) {
        this.meetingID = meetingID;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public Integer getHostID() {
        return hostID;
    }

    public void setHostID(Integer hostID) {
        this.hostID = hostID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<Integer, ArrayList<Integer>> getAttendees() {
        return attendees;
    }

    public void setAttendees(Map<Integer, ArrayList<Integer>> attendees) {
        this.attendees = attendees;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Meeting(Integer hostID, String meetingName, String startDate, String endDate, String date,
                            String location, String description) {
        this.hostID = hostID;
        attendees = new HashMap<>();
        attendees.put(hostID, new ArrayList<>());
        this.meetingName = meetingName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.date = date;
        this.location = location;
        this.description = description;
    }
}
