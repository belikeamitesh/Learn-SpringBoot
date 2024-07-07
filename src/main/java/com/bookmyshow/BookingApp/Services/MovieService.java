package com.bookmyshow.BookingApp.Services;

import com.bookmyshow.BookingApp.Dtos.RequestDtos.MovieEntryDto;
import com.bookmyshow.BookingApp.Exceptions.MovieAlreadyPresentWithSameNameAndLanguage;
import com.bookmyshow.BookingApp.Exceptions.MovieDoesNotExists;
import com.bookmyshow.BookingApp.Models.Movie;
import com.bookmyshow.BookingApp.Models.Show;
import com.bookmyshow.BookingApp.Models.Ticket;
import com.bookmyshow.BookingApp.Repository.MovieRepository;
import com.bookmyshow.BookingApp.Repository.ShowRepository;
import com.bookmyshow.BookingApp.Transformers.MovieTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    public String addMovie(MovieEntryDto movieEntryDto) throws MovieAlreadyPresentWithSameNameAndLanguage {
        if (movieRepository.findByMovieName(movieEntryDto.getMovieName()) != null) {
            if (movieRepository.findByMovieName(movieEntryDto.getMovieName()).getLanguage().equals(movieEntryDto.getLanguage())) {
                throw new MovieAlreadyPresentWithSameNameAndLanguage();
            }
        }
            Movie movie = MovieTransformer.movieDtoToMovie(movieEntryDto);
            movieRepository.save(movie);
            return "The movie has been added successfully";
    }
    public Long totalCollection(Integer movieId) throws MovieDoesNotExists {
        Optional<Movie> movieOpt = movieRepository.findById(movieId);
        if(movieOpt.isEmpty()) {
            throw new MovieDoesNotExists();
        }
        List<Show> showListOfMovie = showRepository.getAllShowsOfMovie(movieId);
        long ammount = 0;
        for(Show show : showListOfMovie) {
            for(Ticket ticket : show.getTicketList()) {
                ammount += (long)ticket.getTotalTicketsPrice();
            }
        }
        return ammount;
    }
}
