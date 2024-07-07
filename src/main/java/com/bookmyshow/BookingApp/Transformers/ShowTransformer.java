package com.bookmyshow.BookingApp.Transformers;

import com.bookmyshow.BookingApp.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.BookingApp.Models.Show;

public class ShowTransformer {

    public static Show showDtoToShow(ShowEntryDto showEntryDto) {
        Show show = Show.builder()
                .time(showEntryDto.getShowStartTime())
                .date(showEntryDto.getShowDate())
                .build();

        return show;
    }

    public static ShowEntryDto showToShowDto(Show show) {
        ShowEntryDto showEntryDto = new ShowEntryDto();
        showEntryDto.setShowDate(show.getDate());
        showEntryDto.setShowStartTime(show.getTime());
        showEntryDto.setMovieId(show.getMovie().getId());
        showEntryDto.setTheaterId(show.getTheater().getId());
        return showEntryDto;
    }
}
