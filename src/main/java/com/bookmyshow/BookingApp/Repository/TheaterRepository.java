package com.bookmyshow.BookingApp.Repository;

import com.bookmyshow.BookingApp.Models.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Integer> {
    Theater findByAddress(String address);
}
