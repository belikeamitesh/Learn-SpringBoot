package com.bookmyshow.BookingApp.Repository;

import com.bookmyshow.BookingApp.Models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Movie findByMovieName(String name);
}
