package com.bookmyshow.BookingApp.Services;

import com.bookmyshow.BookingApp.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.BookingApp.Dtos.RequestDtos.ShowSeatEntryDto;
import com.bookmyshow.BookingApp.Dtos.RequestDtos.ShowTimingsDto;
import com.bookmyshow.BookingApp.Enums.SeatType;
import com.bookmyshow.BookingApp.Exceptions.MovieDoesNotExists;
import com.bookmyshow.BookingApp.Exceptions.ShowDoesNotExists;
import com.bookmyshow.BookingApp.Exceptions.TheaterDoesNotExists;
import com.bookmyshow.BookingApp.Models.*;
import com.bookmyshow.BookingApp.Repository.MovieRepository;
import com.bookmyshow.BookingApp.Repository.ShowRepository;
import com.bookmyshow.BookingApp.Repository.TheaterRepository;
import com.bookmyshow.BookingApp.Transformers.ShowTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {
    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

//    @Autowired
//    private ShowTransformer showTransformer;

    @Autowired
    private TheaterRepository theaterRepository;

    public String addShow(ShowEntryDto showEntryDto) throws MovieDoesNotExists, TheaterDoesNotExists{
        Show show = ShowTransformer.showDtoToShow(showEntryDto);

        Optional<Movie> movieOpt = movieRepository.findById(showEntryDto.getMovieId());
        if(movieOpt.isEmpty()) {
            throw new MovieDoesNotExists();
        }
        Optional<Theater> theaterOpt = theaterRepository.findById(showEntryDto.getTheaterId());
        if(theaterOpt.isEmpty()) {
            throw new TheaterDoesNotExists();
        }

        Theater theater = theaterOpt.get();
        Movie movie = movieOpt.get();

        show.setMovie(movie);
        show.setTheater(theater);
        show = showRepository.save(show);

        movie.getShows().add(show);
        theater.getShowList().add(show);

        movieRepository.save(movie);
        theaterRepository.save(theater);

        return "Show has been added Successfully";
    }
    public String associateShowSeats(ShowSeatEntryDto showSeatEntryDto) throws ShowDoesNotExists{
        Optional<Show> showOpt = showRepository.findById(showSeatEntryDto.getShowId());
        if(showOpt.isEmpty()) {
            throw new ShowDoesNotExists();
        }
        Show show = showOpt.get();
        Theater theater = show.getTheater();

        List<TheaterSeat> theaterSeatList = theater.getTheaterSeatList();

        List<ShowSeat> showSeatList = show.getShowSeatList();
        for(TheaterSeat theaterSeat : theaterSeatList) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setSeatNo(theaterSeat.getSeatNo());
            showSeat.setSeatType(theaterSeat.getSeatType());

            if(showSeat.getSeatType().equals(SeatType.CLASSIC)) {
                showSeat.setPrice((showSeatEntryDto.getPriceOfClassicSeat()));
            } else {
                showSeat.setPrice(showSeatEntryDto.getPriceOfPremiumSeat());
            }

            showSeat.setShow(show);
            showSeat.setIsAvailable(Boolean.TRUE);
            showSeat.setIsFoodContains(Boolean.FALSE);

            showSeatList.add(showSeat);
        }
        showRepository.save(show);

        return "Show seats have been associated successfully";
    }

    public List<Time> showTimingsOnDate(Date date,Integer theaterId,Integer movieId) {
        return showRepository.getShowTimingsOnDate(date, theaterId, movieId);
    }

//    public List<ShowEntryDto> getAllShows(){
//        List<Show> shows = showRepository.findAll();
//        ShowEntryDto showEntryDto = showTransformer.showToShowDto(shows);
//        return  List<showEntryDto>;
//    }
    public String movieHavingMostShows() {
        Integer movieId = showRepository.getMostShowsMovie();
        return movieRepository.findById(movieId).get().getMovieName();
    }
}
