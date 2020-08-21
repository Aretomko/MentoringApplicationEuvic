package com.example.sheduleApp.sheduleApp.repos;

import com.example.sheduleApp.sheduleApp.domain.Consultation;
import com.example.sheduleApp.sheduleApp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ConsultationRepo extends JpaRepository<Consultation, Long> {
    List<Consultation> findByUserWhoReserved(User user);

    List<Consultation> findByDate(LocalDate date);

    List<Consultation> findByUserWhoCreated(User user);
    @Query(value = "SELECT * from Consultation c WHERE c.user_who_reserved is null", nativeQuery = true)
    List<Consultation> findAllNotReservedConsultations();

    @Query(value = "SELECT * from Consultation c WHERE c.user_who_reserved IS NOT NULL", nativeQuery = true)
    List<Consultation> findAllReservedConsultations();
}
