package com.bookmyshow.BookingApp.Transformers;

import com.bookmyshow.BookingApp.Dtos.RequestDtos.TheaterEntryDto;
import com.bookmyshow.BookingApp.Models.Theater;

public class TheaterTransformer {

    public static Theater theaterDtoToTheater(TheaterEntryDto entryDto) {
        Theater theater = Theater.builder()
                .name(entryDto.getName())
                .address(entryDto.getAddress())
                .build();
        return theater;
    }
}
