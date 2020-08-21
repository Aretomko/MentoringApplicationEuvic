package com.example.sheduleApp.sheduleApp.domain.requests;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateMultipleConsultationsRequest {
    private LocalDate date;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private int userWhoCreatedId;

    public int getUserWhoCreatedId() {
        return userWhoCreatedId;
    }

    public void setUserWhoCreatedId(int userWhoCreatedId) {
        this.userWhoCreatedId = userWhoCreatedId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalTime timeTo) {
        this.timeTo = timeTo;
    }
}