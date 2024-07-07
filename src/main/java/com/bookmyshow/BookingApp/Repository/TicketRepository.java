package com.bookmyshow.BookingApp.Repository;

import com.bookmyshow.BookingApp.Models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Integer> {
}