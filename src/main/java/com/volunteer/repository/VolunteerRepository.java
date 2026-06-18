package com.volunteer.repository;

import com.volunteer.entity.Volunteer;
import com.volunteer.entity.Volunteer.VolunteerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    List<Volunteer> findByStatus(VolunteerStatus status);
    boolean existsByEmail(String email);
    long countByStatus(VolunteerStatus status);
    long countByRegistrationDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT v.city, COUNT(v) FROM Volunteer v GROUP BY v.city ORDER BY COUNT(v) DESC")
    List<Object[]> countByCity();

    @Query("SELECT v.availability, COUNT(v) FROM Volunteer v GROUP BY v.availability")
    List<Object[]> countByAvailability();

    @Query("SELECT MONTH(v.registrationDate), COUNT(v) FROM Volunteer v WHERE YEAR(v.registrationDate) = :year GROUP BY MONTH(v.registrationDate)")
    List<Object[]> monthlyRegistrations(@Param("year") int year);

    Optional<Volunteer> findByEmail(String email);


}