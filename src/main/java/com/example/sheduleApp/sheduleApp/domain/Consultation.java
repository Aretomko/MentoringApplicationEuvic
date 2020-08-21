package com.example.sheduleApp.sheduleApp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name="user_who_created_id")
    @NotNull
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private User userWhoCreated;

    @ManyToOne
    @JoinColumn(name="user_who_reserved")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private User userWhoReserved;


    public Consultation() {
    }

    public Consultation(LocalDate date, LocalTime time, User userWhoCreated) {
        this.date = date;
        this.time = time;
        this.userWhoCreated = userWhoCreated;
    }

    public Consultation(LocalDate date, LocalTime time, User userWhoCreated, User userWhoReserved) {
        this.date = date;
        this.time = time;
        this.userWhoCreated = userWhoCreated;
        this.userWhoReserved = userWhoReserved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public User getUserWhoCreated() {
        return userWhoCreated;
    }

    public void setUserWhoCreated(User userWhoCreated) {
        this.userWhoCreated = userWhoCreated;
    }

    public User getUserWhoReserved() {
        return userWhoReserved;
    }

    public void setUserWhoReserved(User userWhoReserved) {
        this.userWhoReserved = userWhoReserved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consultation)) return false;
        Consultation that = (Consultation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time) &&
                Objects.equals(userWhoCreated, that.userWhoCreated) &&
                Objects.equals(userWhoReserved, that.userWhoReserved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, time, userWhoCreated, userWhoReserved);
    }

    public String asJSONString() {
        return "{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", userWhoCreated=" + userWhoCreated +
                ", userWhoReserved=" + userWhoReserved +
                '}';
    }
}
