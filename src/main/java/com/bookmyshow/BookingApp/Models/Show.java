package com.bookmyshow.BookingApp.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SHOWS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer showId;

    private Time time;

    private Date date;

    //For one movie there can be many shows
    @ManyToOne
    @JoinColumn
    private Movie movie;

    //For one theater there can be many shows of different movies
//    The @ManyToOne annotation defines a many-to-one relationship between two entities. it indicates that the Movie and Theater entities have a many-to-one relationship.
//    The child entity (in this case, Movie and Theater) that has the join column (foreign key) is considered the Show of the relationship.
//    The @JoinColumn annotation specifies the foreign key column in the relationship show. It tells JPA which column in the child entity corresponds to the parent entity’s primary key.
    @ManyToOne
    @JoinColumn
    private Theater theater;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    private List<ShowSeat> showSeatList = new ArrayList<>();

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    private List<Ticket> ticketList = new ArrayList<>();
}