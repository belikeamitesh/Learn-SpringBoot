package com.bookmyshow.BookingApp.Dtos.RequestDtos;

import com.bookmyshow.BookingApp.Enums.Genre;
import com.bookmyshow.BookingApp.Enums.Language;
import lombok.Data;

import java.sql.Date;

@Data
public class MovieEntryDto {
    private String movieName;
    private Integer duration;
    private Double rating;
    private Date releaseDate;
    private Genre genre;
    private Language language;
}
